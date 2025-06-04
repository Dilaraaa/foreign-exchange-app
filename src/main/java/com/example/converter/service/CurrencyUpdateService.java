package com.example.converter.service;

import java.util.List;

public interface CurrencyUpdateService {
    void syncSupportedCurrencies(List<String> thirdPartyCurrencies);
}
