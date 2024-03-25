package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.exchange.ExchangeResponseDto;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeService {

    ResponseDto fetchCurrenciesAndSave();

    ExchangeResponseDto performExchangeFromAZN(ExchangeRequestDto exchange);

    ExchangeResponseDto performExchangeToAZN(ExchangeRequestDto exchangeResponseDto);

    String getCurrencyFileContent();
}