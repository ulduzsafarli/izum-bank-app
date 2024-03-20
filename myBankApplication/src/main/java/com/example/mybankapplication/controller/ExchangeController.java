package com.example.mybankapplication.controller;

import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;



    @GetMapping("/fetchCurrenciesAndSave")
    public ResponseEntity<ResponseDto> fetchCurrenciesAndSave() {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.fetchCurrenciesAndSave());
    }

}