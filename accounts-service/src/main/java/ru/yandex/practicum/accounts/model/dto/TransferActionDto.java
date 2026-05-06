package ru.yandex.practicum.accounts.model.dto;

import lombok.Data;

@Data
public class TransferActionDto {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
}
