package com.example.converter.mapper;

import com.example.converter.dto.ConversionHistoryResponse;
import com.example.converter.model.ConversionHistoryEntity;

import java.util.List;

public class MapperUtil {
    public static ConversionHistoryResponse mapToResponse(ConversionHistoryEntity entity) {
        ConversionHistoryResponse response = new ConversionHistoryResponse();
        response.setTransactionId(entity.getTransactionId());
        response.setSourceCurrencyCode(entity.getSourceCurrencyCode());
        response.setTargetCurrencyCode(entity.getTargetCurrencyCode());
        response.setSourceAmount(entity.getSourceAmount());
        response.setExchangeRate(entity.getExchangeRate());
        response.setTransactionDate(entity.getTransactionDate());
        return response;
    }

    public static List<ConversionHistoryResponse> mapToResponse(List<ConversionHistoryEntity> conversionHistoryEntities) {
        return conversionHistoryEntities.stream().map(MapperUtil::mapToResponse).toList();
    }
}
