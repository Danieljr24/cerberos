package com.example.cerberos.graphql;

import com.example.cerberos.dto.LoginRequest;
import com.example.cerberos.service.AuthService;
import com.example.cerberos.util.TokenHolder;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {

    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public String login(@Argument("input") LoginRequest input) {
        String token = authService.login(input);
        TokenHolder.setToken(token); // Almacenar el token en el contexto del hilo
        return token; // Devolver el token al cliente si es necesario
    }

    @MutationMapping
    public String logout() {
        return "Logout exitoso";
    }
}