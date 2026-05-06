package ru.yandex.practicum.mybankfront.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    private Long accountNumber;
    private Long userId;
    private Double balance;
}
