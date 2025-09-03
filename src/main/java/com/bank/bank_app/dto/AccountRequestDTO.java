package com.bank.bank_app.dto;


import java.math.BigDecimal;

public record AccountRequestDTO(
        String owner,
        BigDecimal balance
) {
}
