package ru.yandex.practicum.mybankfront.dto;

import lombok.Data;

import java.util.List;

@Data
public class AccountResponse {
    private UserData userData;
    private Account account;
    private List<UserData> usersToTransfer;
}
