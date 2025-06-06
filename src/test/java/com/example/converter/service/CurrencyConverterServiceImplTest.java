package com.example.converter.service;

import com.example.converter.client.CurrencyFreaksClient;
import com.example.converter.model.ConversionHistoryEntity;
import com.example.converter.repository.ConversionHistoryRepository;
import com.example.converter.service.impl.CurrencyConverterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

public class CurrencyConverterServiceImplTest {

    private CurrencyFreaksClient currencyFreaksClient;
    private ConversionHistoryRepository conversionHistoryRepository;
    private CurrencyConverterServiceImpl service;

    @BeforeEach
    public void setup() {
        currencyFreaksClient = mock(CurrencyFreaksClient.class);
        conversionHistoryRepository = mock(ConversionHistoryRepository.class);
        service = new CurrencyConverterServiceImpl(currencyFreaksClient, conversionHistoryRepository);
    }


    @Test
    public void testGetExchangeRate() {
        when(currencyFreaksClient.fetchExchangeRate("EUR", "USD"))
                .thenReturn(BigDecimal.valueOf(1.09));

        BigDecimal rate = service.getExchangeRate("EUR", "USD");

        assertEquals(BigDecimal.valueOf(1.09), rate);
    }

    @Test
    public void testConvertCsvAndReturnResultFile_validInput() throws Exception {
        String csvContent = "sourceCurrencyCode,targetCurrencyCode,amount\nUSD,EUR,200";
        MultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                new ByteArrayInputStream(csvContent.getBytes()));

        when(currencyFreaksClient.fetchExchangeRate("USD", "EUR"))
                .thenReturn(BigDecimal.valueOf(0.95));

        when(conversionHistoryRepository.save(any())).thenAnswer(invocation -> {
            ConversionHistoryEntity saved = invocation.getArgument(0);
            saved.setTransactionId(2025L);
            return saved;
        });

        byte[] result = service.convertCsvAndReturnResultFile(file);
        assertNotNull(result);
        String csvOutput = new String(result);
        assertTrue(csvOutput.contains("190"));
        assertTrue(csvOutput.contains("200"));
        assertTrue(csvOutput.contains("USD"));
        assertTrue(csvOutput.contains("EUR"));
        assertTrue(csvOutput.contains("SUCCESS"));
        assertTrue(csvOutput.contains("2025"));
    }
}
