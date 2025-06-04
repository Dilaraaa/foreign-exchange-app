package com.example.converter.exception;

public class ExchangeRateNotAvailable extends RuntimeException {
    public ExchangeRateNotAvailable(String from, String to) {
        super(("Exchange rate not available for: " + from + " to " + to));
    }
}
