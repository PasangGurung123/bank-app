package com.bank.bank_app.service.impl;

import com.bank.bank_app.dto.AccountRequestDTO;
import com.bank.bank_app.dto.AccountResponseDT0;
import com.bank.bank_app.dto.EntryDTO;
import com.bank.bank_app.entity.EntryType;
import com.bank.bank_app.exception.AccountNotFoundException;
import com.bank.bank_app.mapper.AccountMapper;
import com.bank.bank_app.model.Account;
import com.bank.bank_app.model.Entry;
import com.bank.bank_app.model.Transfer;
import com.bank.bank_app.repository.AccountRepository;
import com.bank.bank_app.repository.EntryRepository;
import com.bank.bank_app.repository.TransferRepository;
import com.bank.bank_app.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final EntryRepository entryRepository;
    private final TransferRepository transferRepository;

    @Override
    public AccountResponseDT0 createAccount(AccountRequestDTO accountRequestDTO) {
        Account account = AccountMapper.toEntity(accountRequestDTO);
        return AccountMapper.toResponseDTO(accountRepository.save(account));
    }

    @Override
    public List<AccountResponseDT0> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponseDT0 getAccountById(Long id) {
        return accountRepository.findById(id)
                .map(AccountMapper::toResponseDTO)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
    }

    @Override
    public AccountResponseDT0 updateAccount(Long id, AccountRequestDTO accountRequestDTO) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

        account.setOwner(accountRequestDTO.owner());
        account.setBalance(accountRequestDTO.balance());

        return AccountMapper.toResponseDTO(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));
        accountRepository.delete(account);
    }

    @Override
    public AccountResponseDT0 deposit(Long id, BigDecimal amount) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        account.setBalance(account.getBalance().add(amount));
        Account saved = accountRepository.save(account);

        Entry entry = Entry.builder()
                .account(saved)
                .amount(amount)
                .entryType(EntryType.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .balanceAfter(saved.getBalance())
                .build();
        entryRepository.save(entry);
        return AccountMapper.toResponseDTO(saved);
    }

    @Override
    public AccountResponseDT0 withdraw(Long id, BigDecimal amount) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + id));

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }

        if(account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds in account");
        }

        account.setBalance(account.getBalance().subtract(amount));
        Account saved = accountRepository.save(account);

        Entry entry = Entry.builder()
                .account(saved)
                .amount(amount)
                .entryType(EntryType.WITHDRAW)
                .createdAt(LocalDateTime.now())
                .balanceAfter(saved.getBalance())
                .build();
        entryRepository.save(entry);

        return AccountMapper.toResponseDTO(saved);
    }

    @Override
    public List<EntryDTO> getAccountTransactions(Long accountId) {
        return entryRepository.findByAccountId(accountId);
    }

    @Override
    @Transactional
    public AccountResponseDT0 transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + fromAccountId));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + toAccountId));

        if(fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        //Deduct from sender
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        Account savedFrom = accountRepository.save(fromAccount);

        //Add to receiver
        toAccount.setBalance(toAccount.getBalance().add(amount));
        Account savedTo = accountRepository.save(toAccount);

        //Record entries
        Entry fromEntry = Entry.builder()
                .account(savedFrom)
                .amount(amount)
                .entryType(EntryType.TRANSFER_OUT)
                .createdAt(LocalDateTime.now())
                .balanceAfter(savedFrom.getBalance())
                .build();
        entryRepository.save(fromEntry);

        Entry toEntry = Entry.builder()
                .account(savedTo)
                .amount(amount)
                .entryType(EntryType.TRANSFER_IN)
                .createdAt(LocalDateTime.now())
                .balanceAfter(savedTo.getBalance())
                .build();
        entryRepository.save(toEntry);

        //Record Transfers

        Transfer transfer = Transfer.builder()
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .fromAccount(savedFrom)
                .toAccount(savedTo)
                .build();
        transferRepository.save(transfer);

        return AccountMapper.toResponseDTO(savedFrom);
    }

}
