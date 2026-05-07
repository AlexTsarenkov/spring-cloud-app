package ru.yandex.practicum.accounts.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userName) {
        super(String.format("User with username %s not found", userName));
    }
}
