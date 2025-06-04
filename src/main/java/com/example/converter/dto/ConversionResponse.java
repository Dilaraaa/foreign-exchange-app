package com.example.converter.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConversionResponse {
    private BigDecimal amountInTargetCurrency;
    private Long transactionId;
}
