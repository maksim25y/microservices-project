package ru.mudan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.mudan.dto.user.auth.AuthRequest;
import ru.mudan.dto.user.auth.RefreshTokenRequest;
import ru.mudan.dto.user.auth.RegisterRequest;
import ru.mudan.dto.user.auth.TokenResponse;
import ru.mudan.dto.user.event.UserCreatingEvent;
import ru.mudan.dto.user.event.UserUpdatingEvent;
import ru.mudan.exceptions.base.AuthorizationException;
import ru.mudan.exceptions.entity.already_exists.UserAlreadyExistsException;
import ru.mudan.exceptions.entity.not_found.UserNotFoundException;
import ru.mudan.exceptions.keycloak.KeycloakRefreshTokenException;
import ru.mudan.exceptions.keycloak.KeycloakRegistrationException;
import ru.mudan.kafka.KafkaProducer;

@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
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
    @Value("${client.security.token_url}")
    private String tokenUrl;
    private final KafkaProducer kafkaProducer;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public TokenResponse login(AuthRequest authRequest) {
        var response = sendLoginToKeycloak(authRequest);

        return getTokenFromResponse(response);
    }

    @SneakyThrows
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        var response = sendRefreshTokenToKeycloak(request.refreshToken());

        return getTokenFromResponse(response);
    }

    public ResponseEntity<String> sendRefreshTokenToKeycloak(String refreshToken) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        setHeadersForAdmin(map,  "refresh_token");
        map.add("refresh_token", refreshToken);

        var request = new HttpEntity<>(map, headers);

        try {
            return restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
        } catch (RuntimeException e) {
            throw new KeycloakRefreshTokenException();
        }
    }

    private void setHeadersForAdmin(MultiValueMap<String, String> map, String grantType) {
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", grantType);
    }

    private ResponseEntity<String> sendLoginToKeycloak(AuthRequest authRequest) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        setHeadersForAdmin(map,  "password");
        map.add("username", authRequest.email());
        map.add("password", authRequest.password());

        var request = new HttpEntity<>(map, headers);

        try {
            return restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);
        } catch (RuntimeException e) {
            throw new AuthorizationException(authRequest.email());
        }
    }

    public void update(UserUpdatingEvent event) {
        var user = getUserByEmail(event.email());
        var usersResource = getUsersResource();

        user.setFirstName(event.firstname());
        user.setLastName(event.lastname());

        usersResource.get(user.getId()).update(user);
    }

    public void registerUser(RegisterRequest request) {
        if (userAlreadyExists(request.email())) {
            throw new UserAlreadyExistsException(request.email());
        }

        var userRepresentation = createUserRepresentation(request);

        var usersResource = getUsersResource();

        var response = usersResource.create(userRepresentation);

        if (!Objects.equals(201, response.getStatus())) {
            throw new KeycloakRegistrationException(request.email());
        }

        var userCreatingEvent = UserCreatingEvent.builder()
                .email(request.email())
                .firstname(request.firstname())
                .lastname(request.lastname())
                .build();

        kafkaProducer.sendUserCreatingEvent(userCreatingEvent);
    }

    public void delete(String email) {
        var user = getUserByEmail(email);
        var usersResource = getUsersResource();
        usersResource.delete(user.getId());
    }

    private UserRepresentation getUserByEmail(String email) {
        var userResource = getUsersResource();
        var foundUser = userResource.searchByEmail(email, true);

        if (foundUser.isEmpty()) {
            throw new UserNotFoundException(email);
        }

        return foundUser.getFirst();
    }

    private boolean userAlreadyExists(String email) {
        var usersResource = getUsersResource();

        var user = usersResource.searchByUsername(email, true);

        return !user.isEmpty();
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

    private TokenResponse getTokenFromResponse(ResponseEntity<String> response) throws JsonProcessingException {
        var responseMap = objectMapper.readValue(response.getBody(), Map.class);

        var accessToken = (String) responseMap.get("access_token");
        var refreshToken = (String) responseMap.get("refresh_token");

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UsersResource getUsersResource() {
        return keycloak.realm(realm).users();
    }
}
