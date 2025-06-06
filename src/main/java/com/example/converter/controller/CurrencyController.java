package com.example.converter.controller;

import com.example.converter.dto.ConversionHistoryResponse;
import com.example.converter.dto.ConversionResponse;
import com.example.converter.exception.InvalidFileException;
import com.example.converter.exception.RequiredFilterException;
import com.example.converter.service.CurrencyConverterService;
import com.example.converter.validation.ValidCurrency;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/currency")
@AllArgsConstructor
public class CurrencyController {

    private static final int MAX_PAGE_SIZE = 100;
    private final CurrencyConverterService currencyConverterService;

    @GetMapping("/exchange-rate")
    public ResponseEntity<BigDecimal> getExchangeRate(
            @RequestParam @ValidCurrency String from,
            @RequestParam @ValidCurrency String to) {

        return ResponseEntity.ok(currencyConverterService.getExchangeRate(from, to));
    }

    @GetMapping("/conversion")
    public ResponseEntity<ConversionResponse> convert(
            @RequestParam @ValidCurrency String from,
            @RequestParam @ValidCurrency String to,
            @RequestParam BigDecimal amount) {

        return ResponseEntity.ok(currencyConverterService.convert(from, to, amount));
    }

    @GetMapping("/conversion-history")
    public ResponseEntity<Page<ConversionHistoryResponse>> getConversionHistory (
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") Date date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws RequiredFilterException {
        if (transactionId == null && date == null) {
            throw new RequiredFilterException("transactionId", "date");
        }
        int effectiveSize = Math.min(size, MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(page, effectiveSize, Sort.by("transactionDate").descending());

        return ResponseEntity.ok(currencyConverterService.getConversionHistory(transactionId, date, pageable));
    }

    @PostMapping("/bulk-conversion")
    public ResponseEntity<ByteArrayResource> bulkConvertCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            throw new InvalidFileException("CSV");
        }

        ByteArrayResource resource = new ByteArrayResource(currencyConverterService.convertCsvAndReturnResultFile(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=conversion-results.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(resource.contentLength())
                .body(resource);
    }
}
