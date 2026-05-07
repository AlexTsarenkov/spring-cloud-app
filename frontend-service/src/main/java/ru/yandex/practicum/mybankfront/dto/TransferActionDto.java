package ru.yandex.practicum.mybankfront.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferActionDto {
    private Long fromAccountId;
    private Long toAccountId;
    private Double amount;
}
