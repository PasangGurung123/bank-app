package com.bank.bank_app.mapper;

import com.bank.bank_app.dto.AccountRequestDTO;
import com.bank.bank_app.dto.AccountResponseDT0;
import com.bank.bank_app.model.Account;

import java.time.LocalDateTime;

public class AccountMapper {

    public static AccountResponseDT0 toResponseDTO(Account account) {
        return new AccountResponseDT0(
                account.getId(),
                account.getOwner(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }

    public static Account toEntity(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();
        account.setOwner(accountRequestDTO.owner());
        account.setBalance(accountRequestDTO.balance());
        account.setCreatedAt(LocalDateTime.now());
        return account;
    }
}
