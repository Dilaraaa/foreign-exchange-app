package com.example.converter.repository;

import com.example.converter.model.ConversionHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.ZonedDateTime;

public interface ConversionHistoryRepository extends JpaRepository<ConversionHistoryEntity, Long> {
    @Query("""
    SELECT c FROM ConversionHistoryEntity c
    WHERE (:transactionId IS NULL OR c.transactionId = :transactionId)
      AND (:startDate IS NULL OR c.transactionDate BETWEEN :startDate AND :endDate)
    """)
    Page<ConversionHistoryEntity> findByOptionalTransactionIdAndDate(
            @Param("transactionId") Long transactionId,
            @Param("startDate") ZonedDateTime startDate,
            @Param("endDate") ZonedDateTime endDate,
            Pageable pageable
    );
}