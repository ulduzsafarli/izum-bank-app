package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.ExchangeService;
import com.example.mybankapplication.service.FetchingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl extends FetchingUtil implements ExchangeService {
    private static final String URL = "https://www.cbar.az/currencies/19.03.2024.xml";

    public ResponseDto fetchCurrenciesAndSave() {
        log.info("Fetching and saving from URL: {}", URL);
        String xmlData = fetchXmlData(URL);
        if (xmlData != null) {
            String filteredCurrencies = filterCurrencies(xmlData);
            saveCurrenciesToFile(filteredCurrencies);
            log.info("Successfully fetch and save currency from URL: {}", URL);
            return ResponseDto.builder().responseCode("200").responseMessage("Data fetched successfully!").build();
        } else {
            throw new RuntimeException("Failed to fetch XML data from URL");
        }
    }

}
