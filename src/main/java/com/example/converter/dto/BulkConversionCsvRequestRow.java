package com.example.converter.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BulkConversionCsvRequestRow {
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
}
