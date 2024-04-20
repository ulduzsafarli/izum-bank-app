package org.matrix.izumbankapp.service;

import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.exchange.ExchangeResponseDto;

public interface ExchangeService {

    ExchangeResponseDto fromAZN(ExchangeRequestDto exchange);

    ExchangeResponseDto toAZN(ExchangeRequestDto exchangeResponseDto);

}