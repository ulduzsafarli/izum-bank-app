package com.example.mybankapplication.controller;


import com.example.mybankapplication.model.auth.ResponseDto;
import com.example.mybankapplication.service.ExchangeService;
import com.example.mybankapplication.service.impl.ExchangeServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final ExchangeServiceI exchangeServiceI;


    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto> fetchDataFromUrl() {
        return ResponseEntity.status(HttpStatus.OK).body(exchangeService.fetchDataFromUrl());
    }

    @GetMapping("/fetchCurrenciesAndSave")
    public void fetchCurrenciesAndSave() {
        exchangeServiceI.fetchCurrenciesAndSave();

    }

    @GetMapping("/fetching")
    public void fetch() {
        exchangeService.fetch();
    }

}