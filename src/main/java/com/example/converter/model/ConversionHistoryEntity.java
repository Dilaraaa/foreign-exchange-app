package com.example.converter.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "conversion_history")
public class ConversionHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @NotBlank
    private String sourceCurrencyCode;

    @NotBlank
    private String targetCurrencyCode;

    @NotNull
    private BigDecimal sourceAmount;

    @NotNull
    private BigDecimal exchangeRate;

    @NotNull
    private ZonedDateTime transactionDate;
}
