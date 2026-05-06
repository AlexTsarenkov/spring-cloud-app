package ru.yandex.practicum.notification.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NotificationController {
    @PostMapping("/notification")
    public void notification(@RequestBody String message) {
        log.info(message);
    }
}
