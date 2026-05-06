package ru.yandex.practicum.cash.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.cash.service.CashService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashController.class)
@AutoConfigureMockMvc(addFilters = false)
class CashControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CashService cashService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldReturnBadRequestForInvalidCashRequest() throws Exception {
        mockMvc.perform(post("/cash")
                        .contentType("application/json")
                        .content("""
                                {
                                  "action": "PUT",
                                  "accountNumber": 1,
                                  "balance": -10
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}
