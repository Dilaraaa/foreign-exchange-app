package com.example.converter.service;

import com.example.converter.dto.ConversionHistoryResponse;
import com.example.converter.dto.ConversionResponse;
import com.example.converter.exception.RequiredFilterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyConverterService {
    ConversionResponse convert(String from, String to, BigDecimal amount);
    BigDecimal getExchangeRate(String from, String to);
    Page<ConversionHistoryResponse> getConversionHistory(String transactionId, Date date, Pageable pageable) throws RequiredFilterException;
    byte[] convertCsvAndReturnResultFile(MultipartFile file) throws IOException;

}
