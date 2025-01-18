package ru.mudan.dto.user.event;

public class UserCreatedEvent {
    private String username;
    private String firstname;
    private String lastname;
    private String email;

    public UserCreatedEvent(String username, String firstname, String lastname, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public UserCreatedEvent() {
    }
}
