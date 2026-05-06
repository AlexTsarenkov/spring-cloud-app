package ru.yandex.practicum.mybankfront.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityServiceConsumerContractTest {

    @Test
    void shouldUseBearerTokenWhenCallingGateway() {
        RestClient gatewayClient = mock(RestClient.class);
        OAuth2AuthorizedClientManager manager = mock(OAuth2AuthorizedClientManager.class);

        RestClient.RequestBodyUriSpec requestBodyUriSpec = mock(RestClient.RequestBodyUriSpec.class);
        RestClient.RequestBodySpec requestBodySpec = mock(RestClient.RequestBodySpec.class);
        RestClient.ResponseSpec responseSpec = mock(RestClient.ResponseSpec.class);

        when(manager.authorize(any(OAuth2AuthorizeRequest.class))).thenReturn(buildAuthorizedClient("user-token"));
        when(gatewayClient.method(HttpMethod.GET)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("/account-service/accounts")).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(String.class)).thenReturn("ok");

        SecurityService service = new SecurityService(gatewayClient, manager);
        Authentication auth = new UsernamePasswordAuthenticationToken("user", "pwd", AuthorityUtils.NO_AUTHORITIES);
        String result = service.getWithUserToken("/account-service/accounts", String.class, auth);

        assertThat(result).isEqualTo("ok");
    }

    private OAuth2AuthorizedClient buildAuthorizedClient(String tokenValue) {
        ClientRegistration registration = ClientRegistration.withRegistrationId("keycloak")
                .tokenUri("http://localhost/token")
                .clientId("front")
                .clientSecret("secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("http://localhost/auth")
                .redirectUri("http://localhost/callback")
                .scope("openid")
                .build();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                Instant.now(),
                Instant.now().plusSeconds(300),
                Set.of("openid")
        );
        return new OAuth2AuthorizedClient(registration, "user", accessToken);
    }
}
