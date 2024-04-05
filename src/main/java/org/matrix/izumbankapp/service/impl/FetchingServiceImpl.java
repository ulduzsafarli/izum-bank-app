package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.exception.currencies.CurrencyFetchingException;
import org.matrix.izumbankapp.service.FetchingService;
import org.matrix.izumbankapp.util.FetchingUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchingServiceImpl implements FetchingService {

    private static final String URL_PREFIX = "https://www.cbar.az/currencies/";


    private String generateUrlWithDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        return URL_PREFIX + formattedDate + ".xml";
    }

    @Override
    public void fetchCurrencies() {
        String currentUrl = generateUrlWithDate();
        log.info("Fetching and saving from URL: {}", currentUrl);
        String xmlData = FetchingUtil.fetchXmlData(currentUrl);
        if (xmlData != null) {
            String filteredCurrencies = FetchingUtil.filterCurrencies(xmlData);
            FetchingUtil.saveCurrenciesToFile(filteredCurrencies);
            log.info("Successfully fetch and save currency from URL: {}", currentUrl);
        } else {
            throw new CurrencyFetchingException("Failed to fetch XML data from URL");
        }
    }


    @Override
    public String getFile() {
        log.info("Getting the latest currency file");
        String content = FetchingUtil.readCurrencyFile();
        log.info("Successfully read the latest currency file");
        return content;
    }
}
