package com.example.converter.scheduler;

import com.example.converter.client.CurrencyFreaksClient;
import com.example.converter.service.CurrencyUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CurrencySyncScheduler {

    private final CurrencyUpdateService updater;

    private final CurrencyFreaksClient freaksClient;

    @Scheduled(cron = "0 0 13 * * ?") //
    public void update() {
        List<String> thirdPartyCurrencies = fetchSupportedCurrenciesFromThirdParty();
        updater.syncSupportedCurrencies(thirdPartyCurrencies);
    }

    private List<String> fetchSupportedCurrenciesFromThirdParty() {
        return freaksClient.fetchSupportedCurrencyCodes();
    }
}
