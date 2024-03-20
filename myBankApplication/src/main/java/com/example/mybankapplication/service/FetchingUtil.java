package com.example.mybankapplication.service;

import com.example.mybankapplication.enumeration.accounts.CurrencyType;
import com.example.mybankapplication.exception.CurrencyFileSavingException;
import com.example.mybankapplication.exception.CurrencyFilteringException;
import com.example.mybankapplication.exception.CurrencyRateFormatException;
import com.example.mybankapplication.exception.FetchingDataException;
import com.example.mybankapplication.model.CurrencyData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class FetchingUtil {

    private static final String FILEPATH = "currencies.txt";

    public String fetchXmlData(String url) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            throw new FetchingDataException("Failed to fetch XML data from URL: " + e.getMessage(), e);
        }
    }

    public String filterCurrencies(String xmlData) {
        StringBuilder filteredData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(Objects.requireNonNull(xmlData)))) {
            String line;
            CurrencyData currency = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("</Valute>")) continue;

                if (line.contains("<Valute")) {
                    currency = new CurrencyData();
                    currency.setCode(extractAttribute(line));
                } else if (currency != null) {
                    if (line.contains("<Nominal>")) {
                        currency.setNominal(extractTagContent(line).trim()); // Trim extra spaces
                    } else if (line.contains("<Name>")) {
                        currency.setName(extractTagContent(line).trim());  // Trim extra spaces
                    } else if (line.contains("<Value>")) {
                        currency.setValue(Double.valueOf(extractTagContent(line)));
                    }
                }

                if (currency != null && currency.isComplete() && isValidCurrencyCode(currency.getCode())) {
                    filteredData.append(currency).append("\n---------------------------\n");
                    currency = null;
                }
            }
        } catch (IOException e) {
            throw new CurrencyFilteringException("Failed to filter currencies: " + e.getMessage(), e);
        }
        return filteredData.toString().trim(); // Trim any trailing spaces from the final string
    }

    private String extractAttribute(String line) {
        int startIndex = line.indexOf("Code" + "=\"") + "Code".length() + 2;
        int endIndex = line.indexOf("\"", startIndex);
        return line.substring(startIndex, endIndex);
    }

    private String extractTagContent(String line) {
        return line.replaceAll("<[^>]+>([^<]*)<[^>]+>", "$1");
    }

    private boolean isValidCurrencyCode(String currencyCode) {
        return EnumSet.allOf(CurrencyType.class).stream().anyMatch(c -> c.name().equals(currencyCode));
    }


    public void saveCurrenciesToFile(String currencies) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH))) {
            writer.write(currencies);
        } catch (IOException e) {
            throw new CurrencyFileSavingException("Failed to save filtered currencies to file: " + e.getMessage(), e);
        }
    }

    public Map<String, BigDecimal> fetchRates() {
        Map<String, BigDecimal> rates = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILEPATH))) {
            String line;
            String code = null;
            while ((line = reader.readLine()) != null) {
                String trim = line.substring(line.indexOf(":") + 1).trim();
                if (line.startsWith("Currency Code:")) {
                    code = trim;
                } else if (line.startsWith("Currency Rate:")) {
                    if (code != null) {
                        BigDecimal rate = new BigDecimal(trim);
                        rates.put(code, rate);
                    } else {
                        throw new CurrencyFilteringException("Currency code is missing for rate: " + line);
                    }
                }
            }
        } catch (IOException e) {
            throw new CurrencyFilteringException("Failed to read rates from file: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new CurrencyRateFormatException("Invalid rate format, message: " + e.getMessage(), e);
        }

        return rates;
    }

}
