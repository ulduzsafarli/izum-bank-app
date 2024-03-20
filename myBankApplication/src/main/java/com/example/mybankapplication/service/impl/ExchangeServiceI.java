package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.enumeration.accounts.CurrencyType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.EnumSet;
import java.util.Objects;

@Service
public class ExchangeServiceI {
    private static final String URL = "https://www.cbar.az/currencies/19.03.2024.xml";

    public void fetchCurrenciesAndSave() {
        String xmlData = fetchXmlData();
        if (xmlData != null) {
            String filteredCurrencies = filterCurrencies(xmlData);
            saveCurrenciesToFile(filteredCurrencies);
        } else {
            throw new RuntimeException("Failed to fetch XML data from URL");
        }
    }

    private String fetchXmlData() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForObject(URL, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch XML data from URL: " + e.getMessage(), e);
        }
    }

    private String filterCurrencies(String xmlData) {
        StringBuilder filteredData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new StringReader(Objects.requireNonNull(xmlData)))) {
            String line;
            boolean isCurrencyData = false;
            String currencyCode = null;
            String nominal = null;
            String name = null;
            String value = null;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<Valute")) {
                    isCurrencyData = true;
                    currencyCode = line.substring(line.indexOf("Code=\"") + 6, line.indexOf("\"", line.indexOf("Code=\"") + 6));
                } else if (line.contains("</Valute>")) {
                    isCurrencyData = false;
                    if (currencyCode != null && nominal != null && name != null && value != null) {
                        String finalCurrencyCode = currencyCode;
                        if (EnumSet.allOf(CurrencyType.class).stream().anyMatch(c -> c.name().equals(finalCurrencyCode))) {
                            // Структурированная запись в файл
                            filteredData.append("Currency Code: ").append(currencyCode).append("\n")
                                    .append("Currency Nominal: ").append(nominal).append("\n")
                                    .append("Currency Name: ").append(name).append("\n")
                                    .append("Currency Rate: ").append(value).append("\n")
                                    .append("---------------------------").append("\n");
                        }
                    }
                    // Сбросим значения переменных для следующей валюты
                    currencyCode = null;
                    nominal = null;
                    name = null;
                    value = null;
                } else if (isCurrencyData) {
                    if (line.contains("<Nominal>")) {
                        nominal = line.substring(line.indexOf(">") + 1).trim();
                    } else if (line.contains("<Name>")) {
                        name = line.substring(line.indexOf(">") + 1).trim();
                    } else if (line.contains("<Value>")) {
                        value = line.substring(line.indexOf(">") + 1).trim();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to filter currencies: " + e.getMessage(), e);
        }
        return filteredData.toString();
    }



    private void saveCurrenciesToFile(String currencies) {
        String filePath = "currencies.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(currencies);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save filtered currencies to file: " + e.getMessage(), e);
        }
    }
}
