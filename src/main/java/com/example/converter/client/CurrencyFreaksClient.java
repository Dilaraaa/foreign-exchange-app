package com.example.converter.client;

import com.example.converter.exception.ExchangeRateNotAvailable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CurrencyFreaksClient {

    private static final String SUPPORTED_CURRENCIES_API_URL = "https://api.currencyfreaks.com/v2.0/currency-symbols";
    private static final String EXCHANGE_RATE_API_URL = "https://api.currencyfreaks.com/v2.0/rates/latest";
    private static final String API_KEY = "4194eef68c8c46b1ae99812dd4ee5269";

    private final RestTemplate restTemplate = new RestTemplate();

    @Retryable(
            value = { RestClientException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public List<String> fetchSupportedCurrencyCodes() {
        String uri = UriComponentsBuilder.fromHttpUrl(SUPPORTED_CURRENCIES_API_URL)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        Map<String, Object> symbols = (Map<String, Object>) response.get("currencySymbols");

        return symbols.keySet().stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    @Recover
    public List<String> fallback(RestClientException e) {
        log.error("Failed to fetch supported currencies from CurrencyFreaks after 3 attempts", e);
        return Collections.emptyList();
    }

    public BigDecimal fetchExchangeRate(String from, String to) {
        String uri = UriComponentsBuilder.fromHttpUrl(EXCHANGE_RATE_API_URL)
                .queryParam("apikey", API_KEY)
                .queryParam("base", from)
                .queryParam("symbols", to)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(uri, Map.class);
        Map<String, String> rates = (Map<String, String>) response.get("rates");

        if (rates == null || !rates.containsKey(to)) {
            throw new ExchangeRateNotAvailable(from, to);
        }

        return new BigDecimal(rates.get(to));
    }
}

