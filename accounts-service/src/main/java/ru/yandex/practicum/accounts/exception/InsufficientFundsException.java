package ru.yandex.practicum.accounts.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long accountNumber) {
        super(String.format("Insufficient fund for account %d", accountNumber));
    }
}
