package ru.mudan.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/test")
public class TestRestController {

    @GetMapping("/hello")
    public String hello(Authentication authentication) {
        return "Hello, auth " + authentication.getName();
    }

    @GetMapping("/h")
    public String hui() {
        return "Hello!";
    }
}
