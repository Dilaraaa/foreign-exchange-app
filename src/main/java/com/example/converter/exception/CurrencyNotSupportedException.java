package com.example.converter.exception;

public class CurrencyNotSupportedException extends RuntimeException {
    public CurrencyNotSupportedException(String code) {
        super("Currency Code not supported: " + code);
    }
}
