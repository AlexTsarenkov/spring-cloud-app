package ru.yandex.practicum.cash.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.cash.enumeration.CashAction;

@Data
@Builder
public class CashActionDto {
    private CashAction action;
    private Long accountNumber;
    private Double balance;
}
