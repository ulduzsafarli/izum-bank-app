package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import org.matrix.izumbankapp.exception.currencies.CurrencyFetchingException;
import org.matrix.izumbankapp.exception.currencies.UnsupportedCurrencyException;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.exchange.ExchangeResponseDto;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.service.ExchangeService;
import org.matrix.izumbankapp.util.FetchingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private static final String URL_PREFIX = "https://www.cbar.az/currencies/";


    private String generateUrlWithDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(new Date());
        return URL_PREFIX + formattedDate + ".xml";
    }

    @Override
    public ResponseDto fetchCurrenciesAndSave() {
        String currentUrl = generateUrlWithDate();
        log.info("Fetching and saving from URL: {}", currentUrl);
        String xmlData = FetchingUtil.fetchXmlData(currentUrl);
        if (xmlData != null) {
            String filteredCurrencies = FetchingUtil.filterCurrencies(xmlData);
            FetchingUtil.saveCurrenciesToFile(filteredCurrencies);
            log.info("Successfully fetch and save currency from URL: {}", currentUrl);
            return new ResponseDto("Data fetched successfully!");
        } else {
            throw new CurrencyFetchingException("Failed to fetch XML data from URL");
        }
    }

    @Override
    public String getCurrencyFileContent() {
        log.info("Getting the latest currency file");
        String content = FetchingUtil.readCurrencyFile();
        log.info("Successfully read the latest currency file");
        return content;
    }

    @Override
    public ExchangeResponseDto performExchangeFromAZN(ExchangeRequestDto exchange) {
        return performExchange(exchange, true);
    }

    @Override
    public ExchangeResponseDto performExchangeToAZN(ExchangeRequestDto exchange) {
        return performExchange(exchange, false);
    }

    private ExchangeResponseDto performExchange(ExchangeRequestDto exchange, boolean isFromAZN) {
        log.info("Performing exchange {} {} with amount {}", isFromAZN ? "from AZN to" : "to AZN from", exchange.getCurrencyType(),
                exchange.getAmount());

        Map<String, BigDecimal> rates = FetchingUtil.fetchRates();
        if (!rates.containsKey(exchange.getCurrencyType().name())) {
            throw new UnsupportedCurrencyException("Unsupported currency: " + exchange.getCurrencyType());
        }

        BigDecimal rate = rates.get(exchange.getCurrencyType().name());
        BigDecimal originalAmount = BigDecimal.valueOf(exchange.getAmount());
        BigDecimal convertedAmount = isFromAZN ? originalAmount.divide(rate, 2, RoundingMode.HALF_EVEN) :
                originalAmount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);

        log.info("Successfully Performing exchange {} {} with amount {}", isFromAZN ? "from AZN to" : "to AZN from",
                exchange.getCurrencyType(), exchange.getAmount());

        return ExchangeResponseDto.builder()
                .amount(exchange.getAmount())
                .fromCurrency(isFromAZN ? CurrencyType.AZN : exchange.getCurrencyType())
                .toCurrency(isFromAZN ? exchange.getCurrencyType() : CurrencyType.AZN)
                .convertedAmount(convertedAmount.doubleValue())
                .build();
    }

}


