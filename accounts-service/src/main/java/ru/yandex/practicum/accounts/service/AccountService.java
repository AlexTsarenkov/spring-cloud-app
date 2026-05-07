package ru.yandex.practicum.accounts.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.accounts.exception.AccountNotFoundException;
import ru.yandex.practicum.accounts.exception.InsufficientFundsException;
import ru.yandex.practicum.accounts.model.Account;
import ru.yandex.practicum.accounts.repository.AccountsRepository;

import java.util.List;


@Service
@AllArgsConstructor
public class AccountService {
    private final AccountsRepository accountsRepository;

    public Account getAccountByUserId(Long userId) {
        return accountsRepository.findAccountByUserId(userId)
                .orElseGet(() -> createAccountForUser(userId));
    }

    public Account createAccountForUser(Long userId) {
        Account account = Account.builder()
                .userId(userId)
                .balance(100.00) //преветсвенные 100 рублей для теста
                .build();
        return accountsRepository.save(account);
    }

    @Transactional
    public void addCashToBalance(Long accountNumber, Double balance) {
        accountsRepository.addCashToBalance(accountNumber, balance);
    }

    @Transactional
    public void substractCashFromBalance(Long accountNumber, Double balance) {
        accountsRepository.substractCashFromBalance(accountNumber, balance);
    }

    @Transactional
    public void transferCash(Long fromAccountNumber, Long toAccountNumber, Double balance) {
        Account accountFrom = accountsRepository.findAccountByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountNumber));
        if (accountFrom.getBalance() < balance) {
            throw new InsufficientFundsException(fromAccountNumber);
        }
        accountsRepository.substractCashFromBalance(fromAccountNumber, balance);
        accountsRepository.addCashToBalance(toAccountNumber, balance);
    }
}
