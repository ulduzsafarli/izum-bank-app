package org.matrix.izumbankapp.controller;

import jakarta.validation.Valid;
import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.exchange.ExchangeResponseDto;
import org.matrix.izumbankapp.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping("/from-AZN")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeResponseDto fromAZN(@Valid @RequestBody ExchangeRequestDto exchangeResponseDto) {
        return exchangeService.fromAZN(exchangeResponseDto);
    }

    @PostMapping("/to-AZN")
    @ResponseStatus(HttpStatus.OK)
    public ExchangeResponseDto toAZN(@Valid @RequestBody ExchangeRequestDto exchangeResponseDto) {
        return exchangeService.toAZN(exchangeResponseDto);
    }

}