package ru.yandex.practicum.accounts.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long accountNumber) {
        super(String.format("Account with number %d not found", accountNumber));
    }
}
