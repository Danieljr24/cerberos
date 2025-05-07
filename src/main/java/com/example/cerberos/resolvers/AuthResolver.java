package com.example.cerberos.resolvers;

import com.example.cerberos.dto.LoginRequest;
import com.example.cerberos.dto.LoginResponse;
import com.example.cerberos.service.AuthService;
import org.antlr.v4.runtime.Token;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthResolver {

    private final AuthService authService;

    public AuthResolver(AuthService authService) {
        this.authService = authService;
    }

    @MutationMapping
    public LoginResponse login(@Argument("input") LoginRequest request) {
        LoginResponse loginResponse = new LoginResponse();
        System.out.println(request);
         String token = authService.login(request);
         loginResponse.setToken(token);
         loginResponse.setUser(authService.getUser(request.getDocumento()));
            return loginResponse;
    }

    @MutationMapping
    public String logout() {
        return "Logout exitoso";
    }

    // Query to get user role
    @QueryMapping
    public String getRole(@Argument("documento") String documento) {
        return authService.getUserRole(documento);
    }
}
