package org.matrix.izumbankapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.service.impl.ExchangeServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyScheduler {
    private final ExchangeServiceImpl exchangeService;

    @Scheduled(cron = "${CURRENCY_SCHEDULER}")
    public void fetchAndSaveCurrencyData() {
        exchangeService.fetchCurrenciesAndSave();
        log.info("Successfully fetch data for: {}", LocalDate.now());
    }
}
