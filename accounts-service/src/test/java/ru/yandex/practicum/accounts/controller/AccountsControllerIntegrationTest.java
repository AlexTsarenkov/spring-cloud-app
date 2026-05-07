package ru.yandex.practicum.accounts.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.accounts.service.AccountService;
import ru.yandex.practicum.accounts.service.UserDataService;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountsController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountsControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UserDataService userDataService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldReturnBadRequestForInvalidCashDto() throws Exception {
        mockMvc.perform(post("/accounts/cash")
                        .contentType("application/json")
                        .content("""
                                {
                                  "action": "PUT",
                                  "accountNumber": 1,
                                  "balance": 0
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAcceptValidCashContract() throws Exception {
        mockMvc.perform(post("/accounts/cash")
                        .contentType("application/json")
                        .content("""
                                {
                                  "action": "PUT",
                                  "accountNumber": 7,
                                  "balance": 10
                                }
                                """))
                .andExpect(status().isOk());

        verify(accountService).addCashToBalance(7L, 10.0);
    }
}
