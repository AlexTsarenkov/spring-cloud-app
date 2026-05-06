package ru.yandex.practicum.transfer.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
public class TransferService {
    private final RestClient accountsRestClient;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    public void submitTransferOperation(Object body) {
        Authentication principal = new AnonymousAuthenticationToken(
                "cash-service",
                "cash-service",
                AuthorityUtils.createAuthorityList("ROLE_SYSTEM")
        );
        OAuth2AuthorizeRequest authRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId("transfer-service-client-credentials")
                .principal(principal)
                .build();
        OAuth2AuthorizedClient client = authorizedClientManager.authorize(authRequest);
        if (client == null || client.getAccessToken() == null) {
            throw new IllegalStateException("Cannot obtain access token for service request");
        }

        accountsRestClient
                .post()
                .uri("/accounts/transfer")
                .headers(headers -> headers.setBearerAuth(client.getAccessToken().getTokenValue()))
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}

