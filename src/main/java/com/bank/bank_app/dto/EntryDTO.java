package com.bank.bank_app.dto;

import com.bank.bank_app.entity.EntryType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EntryDTO(
        Long id,
        BigDecimal amount,
        EntryType entryType,
        LocalDateTime createdAt,
        BigDecimal balanceAfter
) {
}
