package ru.yandex.practicum.accounts.model.dto;

import lombok.Data;
import ru.yandex.practicum.accounts.enumeration.CashAction;

@Data
public class CashActionDto {
    private CashAction action;
    private Long accountNumber;
    private Double balance;
}
