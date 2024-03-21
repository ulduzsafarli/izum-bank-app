package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.exchange.ExchangeRequestDto;
import com.example.mybankapplication.model.exchange.ExchangeResponseDto;
import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.ExchangeService;
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