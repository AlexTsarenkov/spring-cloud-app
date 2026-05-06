package ru.yandex.practicum.notification.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class NotificationControllerUnitTest {

    @Test
    void shouldAcceptMessageWithoutException() {
        NotificationController controller = new NotificationController();
        assertDoesNotThrow(() -> controller.notification("test-message"));
    }
}
