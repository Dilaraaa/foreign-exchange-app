package com.example.converter.service.impl;

import com.example.converter.client.CurrencyFreaksClient;
import com.example.converter.dto.BulkConversionCsvRequestRow;
import com.example.converter.dto.BulkConversionCsvResponseRow;
import com.example.converter.dto.ConversionHistoryResponse;
import com.example.converter.dto.ConversionResponse;
import com.example.converter.exception.RequiredFilterException;
import com.example.converter.mapper.MapperUtil;
import com.example.converter.model.ConversionHistoryEntity;
import com.example.converter.repository.ConversionHistoryRepository;
import com.example.converter.service.CurrencyConverterService;
import com.example.converter.util.DateTimeUtil;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private final CurrencyFreaksClient currencyFreaksClient;
    private final ConversionHistoryRepository conversionHistoryRepository;

    @Override
    public ConversionResponse convert(String from, String to, BigDecimal amount) {
        BigDecimal rate = currencyFreaksClient.fetchExchangeRate(from, to);
        BigDecimal converted = amount.multiply(rate);

        ConversionHistoryEntity entity = new ConversionHistoryEntity();
        entity.setSourceCurrencyCode(from);
        entity.setTargetCurrencyCode(to);
        entity.setSourceAmount(amount);
        entity.setExchangeRate(rate);
        entity.setTransactionDate(ZonedDateTime.now());

        conversionHistoryRepository.save(entity);

        ConversionResponse response = new ConversionResponse();
        response.setAmountInTargetCurrency(converted);
        response.setTransactionId(entity.getTransactionId());
        return response;
    }

    @Override
    public BigDecimal getExchangeRate(String from, String to) {
        return currencyFreaksClient.fetchExchangeRate(from, to);
    }

    @Override
    public Page<ConversionHistoryResponse> getConversionHistory(String transactionId, Date date, Pageable pageable)
            throws RequiredFilterException {

        if (transactionId == null && date == null) {
            throw new RequiredFilterException("transactionId", "date");
        }

        Long id = transactionId != null ? Long.parseLong(transactionId) : null;
        ZonedDateTime start = null;
        ZonedDateTime end = null;

        if (date != null) {
            start = DateTimeUtil.toStartOfDay(date);
            end = start.plusDays(1);
        }

        Page<ConversionHistoryEntity> page = conversionHistoryRepository
                .findByOptionalTransactionIdAndDate(id, start, end, pageable);

        return page.map(MapperUtil::mapToResponse);
    }

    @Override
    public byte[] convertCsvAndReturnResultFile(MultipartFile file) throws IOException {
        List<BulkConversionCsvResponseRow> resultRows = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<BulkConversionCsvRequestRow> csvToBean = new CsvToBeanBuilder<BulkConversionCsvRequestRow>(reader)
                    .withType(BulkConversionCsvRequestRow.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<BulkConversionCsvRequestRow> inputRows = csvToBean.parse();

            for (BulkConversionCsvRequestRow row : inputRows) {
                BulkConversionCsvResponseRow result = new BulkConversionCsvResponseRow();
                result.setSourceCurrencyCode(row.getSourceCurrencyCode());
                result.setTargetCurrencyCode(row.getTargetCurrencyCode());
                result.setSourceAmount(row.getAmount());

                try {
                    ConversionResponse response = convert(
                            row.getSourceCurrencyCode(),
                            row.getTargetCurrencyCode(),
                            row.getAmount()
                    );
                    result.setAmountInTargetCurrency(response.getAmountInTargetCurrency());
                    result.setTransactionId(response.getTransactionId());
                    result.setStatus("SUCCESS");
                } catch (Exception ex) {
                    result.setStatus("FAIL: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
                }

                resultRows.add(result);
            }
        }

        // Write results to CSV
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                Writer writer = new OutputStreamWriter(out)
        ) {
            StatefulBeanToCsv<BulkConversionCsvResponseRow> beanToCsv =
                    new StatefulBeanToCsvBuilder<BulkConversionCsvResponseRow>(writer)
                            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .build();

            beanToCsv.write(resultRows);
            writer.flush();
            return out.toByteArray();
        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new RuntimeException("Error while writing CSV results", e);
        }
    }
}
