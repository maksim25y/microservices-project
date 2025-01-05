package ru.mudan.service;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import ru.mudan.dto.TokenResponse;

@Service
@RequiredArgsConstructor
public class AuthService {

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
}
