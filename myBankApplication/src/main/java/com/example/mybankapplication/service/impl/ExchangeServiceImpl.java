package com.example.mybankapplication.service.impl;

import com.example.mybankapplication.enumeration.accounts.CurrencyType;
import com.example.mybankapplication.exception.CurrencyFetchingException;
import com.example.mybankapplication.exception.UnsupportedCurrencyException;
import com.example.mybankapplication.model.ExchangeRequestDto;
import com.example.mybankapplication.model.ExchangeResponseDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.ExchangeService;
import com.example.mybankapplication.service.FetchingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private static final String URL = "https://www.cbar.az/currencies/19.03.2024.xml";
    private final FetchingUtil fetchingUtil;

    public ResponseDto fetchCurrenciesAndSave() {
        log.info("Fetching and saving from URL: {}", URL);
        String xmlData = fetchingUtil.fetchXmlData(URL);
        if (xmlData != null) {
            String filteredCurrencies = fetchingUtil.filterCurrencies(xmlData);
            fetchingUtil.saveCurrenciesToFile(filteredCurrencies);
            log.info("Successfully fetch and save currency from URL: {}", URL);
            return ResponseDto.builder().responseCode("200").responseMessage("Data fetched successfully!").build();
        } else {
            throw new CurrencyFetchingException("Failed to fetch XML data from URL");
        }
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

        Map<String, BigDecimal> rates = fetchingUtil.fetchRates();
        if (!rates.containsKey(exchange.getCurrencyType().name())) {
            throw new UnsupportedCurrencyException("Unsupported currency: " + exchange.getCurrencyType());
        }

        BigDecimal rate = rates.get(exchange.getCurrencyType().name());
        BigDecimal originalAmount = BigDecimal.valueOf(exchange.getAmount());
        BigDecimal convertedAmount = isFromAZN ? originalAmount.divide(rate, 2, RoundingMode.HALF_EVEN) :
                originalAmount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);

        return ExchangeResponseDto.builder()
                .amount(exchange.getAmount())
                .fromCurrency(isFromAZN ? CurrencyType.AZN : exchange.getCurrencyType())
                .toCurrency(isFromAZN ? exchange.getCurrencyType() : CurrencyType.AZN)
                .convertedAmount(convertedAmount.doubleValue())
                .build();
    }

}


