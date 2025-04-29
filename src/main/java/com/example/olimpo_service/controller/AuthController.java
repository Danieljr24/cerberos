package com.example.olimpo_service.controller;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.service.AuthService;
import com.example.olimpo_service.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        authService.setTicketCookie(token, response); // Establece cookie con ticket
        return token;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Token") String token) {

        String username = jwtUtil.extractUsername(token);
        System.out.println("Username: " + username);
        return ResponseEntity.ok("Bienvenido " + username);
    }

}
