package ru.yandex.practicum.cash.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cash.dto.CashActionDto;
import ru.yandex.practicum.cash.service.CashService;

@RestController
@AllArgsConstructor
public class CashController {
    private final CashService cashService;

    @PostMapping("/cash")
    public void editCash(@RequestBody CashActionDto cashActionDto) {
        cashService.submitCashOperation(cashActionDto);
    }
}
