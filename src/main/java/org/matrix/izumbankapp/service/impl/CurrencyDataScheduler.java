package org.matrix.izumbankapp.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyDataScheduler {
    private final ExchangeServiceImpl exchangeService;

    @Scheduled(cron = "0 0 9 * * *")
    public void fetchAndSaveCurrencyData() {
        exchangeService.fetchCurrenciesAndSave();
        log.info("Successfully fetch data for: {}", LocalDate.now());
    }
}
