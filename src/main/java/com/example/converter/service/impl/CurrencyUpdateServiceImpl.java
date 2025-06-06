package com.example.converter.service.impl;

import com.example.converter.cache.SupportedCurrencyCache;
import com.example.converter.model.CurrencyEntity;
import com.example.converter.repository.CurrencyRepository;
import com.example.converter.service.CurrencyUpdateService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CurrencyUpdateServiceImpl implements CurrencyUpdateService {

    private final CurrencyRepository repository;
    private final SupportedCurrencyCache cache;

    @Transactional
    public void syncSupportedCurrencies(List<String> thirdPartyCurrencies) {
        repository.deleteAllInBatch();
        repository.saveAll(thirdPartyCurrencies.stream()
                .map(code -> new CurrencyEntity(code.toUpperCase()))
                .collect(Collectors.toList()));
        cache.reload();
    }
}

