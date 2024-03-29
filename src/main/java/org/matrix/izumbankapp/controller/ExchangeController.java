package org.matrix.izumbankapp.controller;

import org.matrix.izumbankapp.model.exchange.ExchangeRequestDto;
import org.matrix.izumbankapp.model.exchange.ExchangeResponseDto;
import org.matrix.izumbankapp.model.auth.ResponseDto;
import org.matrix.izumbankapp.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/fetch-currencies")
    public ResponseEntity<ResponseDto> fetchCurrenciesAndSave() {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.fetchCurrenciesAndSave());
    }
    @GetMapping("/currency-file")
    public ResponseEntity<String> getCurrencyFile() {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.getCurrencyFileContent());
    }

    @PostMapping("/exchange-from-AZN")
    public ResponseEntity<ExchangeResponseDto> exchangeFromAZN(@RequestBody ExchangeRequestDto exchangeResponseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.performExchangeFromAZN(exchangeResponseDto));
    }
    @PostMapping("/exchange-to-AZN")
    public ResponseEntity<ExchangeResponseDto> exchangeToAZN(@RequestBody ExchangeRequestDto exchangeResponseDto) {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.performExchangeToAZN(exchangeResponseDto));
    }

}