package com.example.mybankapplication.service;

import com.example.mybankapplication.model.ExchangeRequestDto;
import com.example.mybankapplication.model.ExchangeResponseDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface ExchangeService {

    ResponseDto fetchCurrenciesAndSave();

    ExchangeResponseDto performExchangeFromAZN(ExchangeRequestDto exchange);

    ExchangeResponseDto performExchangeToAZN(ExchangeRequestDto exchangeResponseDto);
}