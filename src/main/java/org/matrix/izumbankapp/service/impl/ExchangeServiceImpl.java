package org.matrix.izumbankapp.service.impl;

import org.matrix.izumbankapp.enumeration.accounts.CurrencyType;
import org.matrix.izumbankapp.exception.currencies.UnsupportedCurrencyException;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.exchange.ExchangeResponseDto;
import org.matrix.izumbankapp.service.ExchangeService;
import org.matrix.izumbankapp.util.FetchingUtil;
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

    @Override
    public ExchangeResponseDto fromAZN(ExchangeRequestDto exchange) {
        return performExchange(exchange, true);
    }

    @Override
    public ExchangeResponseDto toAZN(ExchangeRequestDto exchange) {
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


