package com.example.olimpo_service.controller;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        authService.setTicketCookie(token, response);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String documento = authentication.getName();
            return ResponseEntity.ok("Bienvenido " + documento);
        }
        return ResponseEntity.status(401).body("No autenticado");
    }
}