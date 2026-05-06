package ru.yandex.practicum.transfer.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.transfer.service.TransferService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransferControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransferService transferService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldReturnBadRequestForInvalidTransferRequest() throws Exception {
        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content("""
                                {
                                  "fromAccountId": 1,
                                  "toAccountId": 2,
                                  "amount": -50
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
