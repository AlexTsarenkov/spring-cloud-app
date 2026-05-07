package ru.yandex.practicum.transfer.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.transfer.dto.TransferActionDto;
import ru.yandex.practicum.transfer.service.TransferService;

@RestController
@AllArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/transfer")
    public void editCash(@Valid @RequestBody TransferActionDto transferActionDto) {
        transferService.submitTransferOperation(transferActionDto);
    }
}
