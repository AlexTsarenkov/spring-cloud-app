package ru.yandex.practicum.mybankfront.dto;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.mybankfront.enumeration.CashAction;

@Data
@Builder
public class CashActionDto {
    private CashAction action;
    private Long accountNumber;
    private Double balance;
}
