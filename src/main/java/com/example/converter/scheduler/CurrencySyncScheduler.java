package com.example.converter.scheduler;

import com.example.converter.client.CurrencyFreaksClient;
import com.example.converter.service.CurrencyUpdateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class CurrencySyncScheduler {

    private final CurrencyUpdateService updater;
    private final CurrencyFreaksClient freaksClient;

    @Scheduled(cron = "0 0 13 * * ?") //
    public void update() {
    try {
        List<String> thirdPartyCurrencies = fetchSupportedCurrenciesFromThirdParty();
        if(thirdPartyCurrencies != null && !thirdPartyCurrencies.isEmpty()) {
            updater.syncSupportedCurrencies(thirdPartyCurrencies);
        } else {
            log.warn("Currency update skipped: fallback or empty response received from third-party.");
        }
    } catch (Exception ex) {
        log.error("Unexpected error while updating supported currencies", ex);
        }
    }

    private List<String> fetchSupportedCurrenciesFromThirdParty() {
        return freaksClient.fetchSupportedCurrencyCodes();
    }
}
