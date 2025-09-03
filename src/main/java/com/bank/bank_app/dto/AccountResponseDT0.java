package com.bank.bank_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDT0(
        Long id,
        String owner,
        BigDecimal balance,
        LocalDateTime createdAt
) {
}
