package com.example.converter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "supported_currency")
public class CurrencyEntity {

    @Id
    private String code;

    public CurrencyEntity() {}

    public CurrencyEntity(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
