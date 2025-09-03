package com.bank.bank_app.controller;

import com.bank.bank_app.dto.AccountResponseDT0;
import com.bank.bank_app.dto.AccountRequestDTO;
import com.bank.bank_app.dto.EntryDTO;
import com.bank.bank_app.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDT0 createAccount(@RequestBody AccountRequestDTO accountRequestDTO) {
        return accountService.createAccount(accountRequestDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponseDT0> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDT0 getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDT0 updateAccount(
            @PathVariable Long id,
            @RequestBody AccountRequestDTO accountRequestDTO
    ) {
        return accountService.updateAccount(id, accountRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @PostMapping("/{id}/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDT0 deposit(@PathVariable Long id,
                                      @RequestParam BigDecimal amount) {
        return accountService.deposit(id, amount);
    }

    @PostMapping("/{id}/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDT0 withdraw(@PathVariable Long id,
                                       @RequestParam BigDecimal amount) {
        return accountService.withdraw(id, amount);
    }

    @GetMapping("/{id}/transactions")
    @ResponseStatus(HttpStatus.OK)
    public List<EntryDTO> getAccountTransactions(@PathVariable Long id) {
        return accountService.getAccountTransactions(id);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponseDT0 transfer(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount
    ) {
        return accountService.transfer(fromAccountId, toAccountId, amount);
    }

}
