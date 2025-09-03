package com.bank.bank_app.service;

import com.bank.bank_app.dto.AccountResponseDT0;
import com.bank.bank_app.dto.AccountRequestDTO;
import com.bank.bank_app.dto.EntryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountResponseDT0 createAccount(AccountRequestDTO accountRequestDTO);
    List<AccountResponseDT0> getAllAccounts();
    AccountResponseDT0 getAccountById(Long id);
    AccountResponseDT0 updateAccount(Long id, AccountRequestDTO accountRequestDTO);
    void deleteAccount(Long id);

    AccountResponseDT0 deposit(Long id, BigDecimal amount);
    AccountResponseDT0 withdraw(Long id, BigDecimal amount);

    //Entries

    List<EntryDTO> getAccountTransactions(Long id);

    AccountResponseDT0 transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
