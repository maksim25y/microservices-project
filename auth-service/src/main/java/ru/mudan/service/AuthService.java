package ru.mudan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.mudan.dto.AuthRequest;
import ru.mudan.dto.RegisterRequest;
import ru.mudan.dto.TokenResponse;

@SuppressWarnings("MagicNumber")
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final Keycloak keycloak;
    @Value("${app.keycloak.realm}")
    private String realm;
    @Value("${client.security.client_id}")
    private String clientId;
    @Value("${client.security.client_secret}")
    private String clientSecret;
    @Value("${client.security.grant_type}")
    private String grantType;
    @Value("${client.security.token_url}")
    private String tokenUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public TokenResponse login(AuthRequest authRequest) {
        var response = sendLoginToKeycloak(authRequest);

        var responseMap = objectMapper.readValue(response.getBody(), Map.class);

        var accessToken = (String) responseMap.get("access_token");
        var refreshToken = (String) responseMap.get("refresh_token");

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private ResponseEntity<String> sendLoginToKeycloak(AuthRequest authRequest) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("username", authRequest.username());
        map.add("password", authRequest.password());
        map.add("grant_type", grantType);

        var request = new HttpEntity<>(map, headers);

        return restTemplate.postForEntity(tokenUrl, request, String.class);
    }

    public void registerUserKeycloak(RegisterRequest request) {
        var userRepresentation = createUserRepresentation(request);

        var usersResource = getUsersResource();

        var response = usersResource.create(userRepresentation);

        if (!Objects.equals(201, response.getStatus())) {
            var responseBody = response.readEntity(String.class);
            log.error("Error in creating user, response body: {}", responseBody);
        }

        //TODO - многопоточность добавить
        var user = usersResource.searchByUsername(request.email(), true);
        var a = user.getFirst();
        sendVerificationEmail(a.getId());
    }

    public void sendVerificationEmail(String userId) {
        var usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    private UserRepresentation createUserRepresentation(RegisterRequest request) {
        var userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setFirstName(request.firstname());
        userRepresentation.setLastName(request.lastname());
        userRepresentation.setUsername(request.email());
        userRepresentation.setEmail(request.email());
        userRepresentation.setEmailVerified(false);

        var credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(request.password());
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }
}
