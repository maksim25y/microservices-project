package ru.mudan.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mudan.dto.RegisterRequest;

@SuppressWarnings("MagicNumber")
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final Keycloak keycloak;
    @Value("${app.keycloak.realm}")
    private String realm;

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
