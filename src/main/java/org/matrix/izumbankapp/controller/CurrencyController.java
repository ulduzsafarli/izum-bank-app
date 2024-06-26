package org.matrix.izumbankapp.controller;

import lombok.RequiredArgsConstructor;
import org.matrix.izumbankapp.service.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void fetch() {
        currencyService.fetch();
    }

    @GetMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public String getFile() {
        return currencyService.getFile();
    }
}
