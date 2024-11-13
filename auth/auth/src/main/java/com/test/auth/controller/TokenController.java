package com.test.auth.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@RestController
@RequestMapping("/token")
public class TokenController {

    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    private final OAuth2AuthorizedClientService authorizedClientService;

    public TokenController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/get")
    public ResponseEntity<String> getToken(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            logger.error("Authentication is not of type OAuth2AuthenticationToken");
            return ResponseEntity.status(403).body("User is not authenticated via OAuth2");
        }

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = oauthToken.getAuthorizedClientRegistrationId();
        String principalName = oauthToken.getName();

        logger.info("Attempting to fetch token for client registration ID: {}", registrationId);

        try {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService
                    .loadAuthorizedClient(registrationId, principalName);

            if (authorizedClient == null) {
                logger.error("Authorized client is null. No token available.");
                return ResponseEntity.status(403).body("No authorized client found");
            }

            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

            if (accessToken != null) {
                logger.info("Access Token: {}", accessToken.getTokenValue());
                return ResponseEntity.ok(accessToken.getTokenValue());
            } else {
                logger.error("Access token is null or expired for client: {}", registrationId);
                return ResponseEntity.status(403).body("Access token not found or expired");
            }
        } catch (Exception e) {
            logger.error("Error fetching token: ", e);
            return ResponseEntity.status(500).body("Error fetching token: " + e.getMessage());
        }
    }
}