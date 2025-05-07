package com.example.cerberos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.cerberos.dto.LoginRequest;
import com.example.cerberos.dto.RegisterRequest;
import com.example.cerberos.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        authService.setTicketCookie(token, response);

        return ResponseEntity.ok().body("Login exitoso");
    }

    @GetMapping("/check-role")
    public ResponseEntity<?> checkUserRole(@RequestParam String documento, @RequestParam String roleName) {
        boolean hasRole = authService.hasRole(documento, roleName);
        if (hasRole) {
            return ResponseEntity.ok("El usuario tiene el rol: " + roleName);
        } else {
            return ResponseEntity.status(404).body("El usuario no tiene el rol: " + roleName);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String result = authService.register(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            String documento = authentication.getName();
            return ResponseEntity.ok("Bienvenido " + documento);
        }
        return ResponseEntity.status(401).body("No autenticado");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        authService.clearTicketCookie(response);

        return ResponseEntity.ok("Logout exitoso");
    }
}
