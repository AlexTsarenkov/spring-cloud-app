package ru.yandex.practicum.mybankfront.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final RestClient gatewayRestClient;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public <T> T getWithUserToken(String uri, Class<T> responseType, Authentication authentication) {
        return exchangeWithUserToken(uri, HttpMethod.GET, null, responseType, authentication);
    }

    public <T> T postWithUserToken(String uri, Class<T> responseType, Authentication authentication, Object body) {
        return exchangeWithUserToken(uri, HttpMethod.POST, body, responseType, authentication);
    }

    public <T> T exchangeWithUserToken(
            String uri,
            HttpMethod method,
            Object body,
            Class<T> responseType,
            Authentication authentication
    ) {
        OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak") // registrationId для login через OIDC
                .principal(authentication)
                .build();

        OAuth2AuthorizedClient client = authorizedClientManager.authorize(authRequest);
        String token = client.getAccessToken().getTokenValue();

        RestClient.RequestBodyUriSpec request = gatewayRestClient.method(method);
        RestClient.RequestBodySpec requestSpec = request
                .uri(uri)
                .headers(h -> h.setBearerAuth(token));

        if (body != null) {
            requestSpec.body(body);
        }

        return requestSpec.retrieve().body(responseType);
    }
}

