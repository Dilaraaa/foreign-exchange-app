package com.example.converter.validation;

import com.example.converter.cache.SupportedCurrencyCache;
import com.example.converter.exception.CurrencyNotSupportedException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrency, String> {

    @Autowired
    private SupportedCurrencyCache cache;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || !cache.isValid(value)) {
            context.disableDefaultConstraintViolation();
            throw new CurrencyNotSupportedException(value);
        }
        return true;
    }
}
