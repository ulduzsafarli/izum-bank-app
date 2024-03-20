package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import com.example.mybankapplication.enumeration.accounts.CurrencyType;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private final static String urlString = "https://www.cbar.az/currencies/19.03.2024.xml";
    private final Set<CurrencyType> allowedCurrencies = EnumSet.copyOf(Arrays.asList(CurrencyType.values()));

    public ResponseDto fetchDataFromUrl() {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (InputStream inputStream = connection.getInputStream()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(inputStream);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("Valute");
                Set<String> filteredCurrencies = new HashSet<>();
                log.info("filteredCurrencies");

                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    log.info("nNode");

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        log.info("eElement");

                        String currencyCode = getTextContent(eElement, "Code");
                        log.info("currencyCode");

                        if (currencyCode != null && allowedCurrencies.contains(CurrencyType.valueOf(currencyCode))) {
                            String currencyName = getTextContent(eElement, "Name");
                            String currencyRate = getTextContent(eElement, "Value");
                            String currencyText = "Currency Code: " + currencyCode + "\n" +
                                    "Currency Name: " + currencyName + "\n" +
                                    "Currency Rate: " + currencyRate + "\n---------------------------";
                            filteredCurrencies.add(currencyText);
                            log.info("Currency Name: {}, Currency Rate: {}", currencyName, currencyRate);
                        }
                    }
                }

                saveFilteredCurrenciesToFile(filteredCurrencies);

                return ResponseDto.builder().responseCode("200").responseMessage("Data fetched successfully!").build();

            } catch (ParserConfigurationException | SAXException e) {
                throw new RuntimeException("Failed to parse XML: " + e.getMessage(), e);
            }
        } catch (MalformedURLException | ProtocolException e) {
            throw new IllegalArgumentException("Invalid URL: " + urlString, e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch data from URL: " + e.getMessage(), e);
        }
    }

    private String getTextContent(Element element, String tagName) {
        Node node = element.getElementsByTagName(tagName).item(0);
        return node != null ? node.getTextContent() : null;
    }

    private void saveFilteredCurrenciesToFile(Set<String> currencies) {
        String outputPath = "filtered_currencies.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (String currency : currencies) {
                writer.write(currency);
                writer.newLine();
            }
            log.info("Filtered currencies saved to file: {}", outputPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save filtered currencies to file: " + e.getMessage(), e);
        }
    }

    public void fetch() {
        String urlString = "https://www.cbar.az/currencies/19.03.2024.xml";
        String outputPath = "filtered_currencies.txt";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(urlString).openStream(), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }

            log.info("Data from the URL has been successfully saved to the file: " + outputPath);
        } catch (IOException e) {
            log.error("An error occurred: " + e.getMessage());
            throw new RuntimeException("Failed to fetch data from url: " + e.getMessage());
        }
    }
}
