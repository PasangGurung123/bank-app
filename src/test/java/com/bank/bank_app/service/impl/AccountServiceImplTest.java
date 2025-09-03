/*

- Unit Testing

when(accountRepository.findById(1L)).thenReturn(Optional.of(account));



🔎 What it does
* accountRepository is a mock (created with @Mock in Mockito).
* You’re telling Mockito: “When someone calls accountRepository.findById(1L), don’t hit the real database. Instead, just return Optional.of(account).” 
So, in your test:
* You control the behavior of the repository.
* You can test your service logic without needing a real DB.


verify(accountRepository, times(1)).findById(1L);


It checks that the mock accountRepository’s method findById(1L) was called exactly once during the test.
If it was called 0 times, or more than once → the test fails.



MockitoAnnotations.openMocks(this);


When you write unit tests with Mockito, you often use annotations like:
* @Mock → creates a mock instance.
* @InjectMocks → creates an instance of the class under test, and injects the mocks into it.
* @Spy → creates a partial mock (real object, but some methods can be stubbed).
By default, these annotations don’t do anything unless you initialize them. That’s what MockitoAnnotations.openMocks(this) does → it scans your test class and initializes all fields annotated with Mockito annotations.
 */


package com.bank.bank_app.service.impl;

import com.bank.bank_app.dto.AccountResponseDT0;
import com.bank.bank_app.exception.AccountNotFoundException;
import com.bank.bank_app.model.Account;
import com.bank.bank_app.model.Entry;
import com.bank.bank_app.model.Transfer;
import com.bank.bank_app.repository.AccountRepository;
import com.bank.bank_app.repository.EntryRepository;
import com.bank.bank_app.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    private Account fromAccount;
    private Account toAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = Account.builder()
                .id(1L)
                .owner("John Doe")
                .balance(BigDecimal.valueOf(1000.00))
                .build();


        fromAccount = Account.builder()
                .id(2L)
                .owner("Pasang Gurung")
                .balance(BigDecimal.valueOf(1000.00))
                .build();

        toAccount = Account.builder()
                .id(3L)
                .owner("Kaushal Gurung")
                .balance(BigDecimal.valueOf(500.00))
                .build();
    }

    @Test
    void testSuccessfulTransfers() {
        when(accountRepository.findById(2L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(3L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));
        when(entryRepository.save(any(Entry.class))).thenAnswer(i -> i.getArgument(0));
        when(transferRepository.save(any(Transfer.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponseDT0 accountResponseDT0 = accountService.transfer(2L, 3L, BigDecimal.valueOf(200.00));

        assertNotNull(accountResponseDT0);
        assertEquals(BigDecimal.valueOf(800.00), accountResponseDT0.balance());

        verify(accountRepository,times(2)).save(any(Account.class));
        verify(entryRepository,times(2)).save(any(Entry.class));
        verify(transferRepository,times(1)).save(any(Transfer.class));

    }

    @Test
    void testTransferInsufficientFunds(){
        when(accountRepository.findById(2L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(3L)).thenReturn(Optional.of(account));

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.transfer(2L, 3L, BigDecimal.valueOf(10000.00)));

        assertEquals("Insufficient funds", exception.getMessage());

        verify(accountRepository, never()).save(fromAccount);
        verify(entryRepository, never()).save(any(Entry.class));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void testTransferNegativeAmount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                accountService.transfer(1L, 2L, BigDecimal.valueOf(-100.00)));

        assertEquals("Transfer amount must be greater than zero", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(entryRepository, never()).save(any(Entry.class));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void testTransferFromAccountNotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.transfer(2L, 3L, BigDecimal.valueOf(100.00)));

        assertEquals("Account not found with id: 2", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(entryRepository, never()).save(any(Entry.class));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void testTransferToAccountNotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(3L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(AccountNotFoundException.class, () ->
                accountService.transfer(2L, 3L, BigDecimal.valueOf(100.00)));

        assertEquals("Account not found with id: 3", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
        verify(entryRepository, never()).save(any(Entry.class));
        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void testGetAccountById_Success() {
        // Stubbing repository call
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        AccountResponseDT0 accountResponseDT0 = accountService.getAccountById(1L);

        assertNotNull(accountResponseDT0);
        assertEquals("John Doe", accountResponseDT0.owner());
        assertEquals(BigDecimal.valueOf(1000.00), accountResponseDT0.balance());

        // Verify repository interaction
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAccountById_NotFound() {
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(2L));
    }

    @Test
    void testDeposit_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        // Stub save to just return the same object
        when(accountRepository.save(any(Account.class))).thenAnswer(i-> i.getArgument(0));

        AccountResponseDT0 accountResponseDT0 = accountService.deposit(1L, BigDecimal.valueOf(5000.00));

        assertEquals(BigDecimal.valueOf(6000.00), accountResponseDT0.balance());

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testDeposit_Invalid() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class, () -> accountService.deposit(1L, BigDecimal.valueOf(-1000.00)));
    }

    @Test
    void testWithdraw_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenAnswer(i -> i.getArgument(0));

        AccountResponseDT0 accountResponseDT0 = accountService.withdraw(1L, BigDecimal.valueOf(200.00));

        assertEquals(BigDecimal.valueOf(800.00), accountResponseDT0.balance());

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testWithdraw_InsufficientFunds(){
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw(1L, BigDecimal.valueOf(2000.00)));
    }

}
