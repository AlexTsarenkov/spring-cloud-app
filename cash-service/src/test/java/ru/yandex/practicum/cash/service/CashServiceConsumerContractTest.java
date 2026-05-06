package ru.yandex.practicum.cash.service;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestClient;
import ru.yandex.practicum.cash.dto.CashActionDto;
import ru.yandex.practicum.cash.enumeration.CashAction;

import java.io.IOException;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CashServiceConsumerContractTest {
    private MockWebServer accountsServer;
    private MockWebServer notificationsServer;

    @BeforeEach
    void setUp() throws IOException {
        accountsServer = new MockWebServer();
        notificationsServer = new MockWebServer();
        accountsServer.start();
        notificationsServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        accountsServer.shutdown();
        notificationsServer.shutdown();
    }

    @Test
    void shouldCallNotificationsAndAccountsWithBearerToken() throws Exception {
        accountsServer.enqueue(new MockResponse().setResponseCode(200));
        notificationsServer.enqueue(new MockResponse().setResponseCode(200));

        RestClient accountsClient = RestClient.builder()
                .baseUrl(accountsServer.url("/").toString())
                .build();
        RestClient notificationsClient = RestClient.builder()
                .baseUrl(notificationsServer.url("/").toString())
                .build();

        OAuth2AuthorizedClientManager manager = mock(OAuth2AuthorizedClientManager.class);
        when(manager.authorize(any())).thenReturn(buildAuthorizedClient("service-token"));

        CashService service = new CashService(accountsClient, notificationsClient, manager);
        service.submitCashOperation(CashActionDto.builder()
                .action(CashAction.PUT)
                .accountNumber(1L)
                .balance(100.0)
                .build());

        RecordedRequest notificationRequest = notificationsServer.takeRequest();
        assertThat(notificationRequest.getPath()).isEqualTo("/notification");
        assertThat(notificationRequest.getHeader("Authorization")).isEqualTo("Bearer service-token");

        RecordedRequest accountsRequest = accountsServer.takeRequest();
        assertThat(accountsRequest.getPath()).isEqualTo("/accounts/cash");
        assertThat(accountsRequest.getHeader("Authorization")).isEqualTo("Bearer service-token");
    }

    private OAuth2AuthorizedClient buildAuthorizedClient(String tokenValue) {
        ClientRegistration registration = ClientRegistration.withRegistrationId("cash-service-client-credentials")
                .tokenUri("http://localhost/token")
                .clientId("cash-service")
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                Instant.now(),
                Instant.now().plusSeconds(300),
                Set.of("test")
        );
        return new OAuth2AuthorizedClient(registration, "cash-service", accessToken);
    }
}
