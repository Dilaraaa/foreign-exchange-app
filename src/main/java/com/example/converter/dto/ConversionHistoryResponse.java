package com.example.converter.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class ConversionHistoryResponse {
    private Long transactionId;
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal sourceAmount;
    private BigDecimal exchangeRate;
    private ZonedDateTime transactionDate;
}
