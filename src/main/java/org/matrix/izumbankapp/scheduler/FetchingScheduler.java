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
public class FetchingScheduler {
    private final FetchingService fetchingService;

    @Scheduled(cron = "${FETCHING_SCHEDULER}")
    public void fetchAndSaveCurrencyData() {
        fetchingService.fetch();
        log.info("Successfully fetch data for: {}", LocalDate.now());
    }
}
