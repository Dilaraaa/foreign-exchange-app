package com.example.converter.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BulkConversionCsvResponseRow {
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal sourceAmount;
    private BigDecimal amountInTargetCurrency;
    private Long transactionId;
    private String status;
}


