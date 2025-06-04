package com.example.converter.cache;

import com.example.converter.repository.CurrencyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SupportedCurrencyCache {

    private final CurrencyRepository currencyRepository;
    private Set<String> supportedCurrencies;

    public SupportedCurrencyCache(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @PostConstruct
    public void init() {
        reload();
    }

    public void reload() {
        supportedCurrencies = currencyRepository.findAll().stream()
                .map(c -> c.getCode().toUpperCase())
                .collect(Collectors.toSet());
    }

    public boolean isValid(String code) {
        return supportedCurrencies.contains(code.toUpperCase());
    }
}
