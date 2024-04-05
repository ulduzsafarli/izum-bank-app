package org.matrix.izumbankapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.matrix.izumbankapp.service.FetchingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyScheduler {
    private final FetchingService fetchingService;

    @Scheduled(cron = "${CURRENCY_SCHEDULER}")
    public void fetchAndSaveCurrencyData() {
        fetchingService.fetchCurrencies();
        log.info("Successfully fetch data for: {}", LocalDate.now());
    }
}
