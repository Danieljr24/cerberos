package com.example.olimpo_service.service;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.entities.User;
import com.example.olimpo_service.entities.UserRole;
import com.example.olimpo_service.repository.UserRepository;
import com.example.olimpo_service.repository.UserRoleRepository;
import com.example.olimpo_service.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public String login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getDocumento(), request.getPassword()));

        var user = userRepository.findByDocumentoWithRoles(request.getDocumento())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var roles = user.getRoles().stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toList());

        return jwtUtil.generateToken(user.getDocumento(), roles);
    }

    public String register(RegisterRequest request) {
        try {
            User user = new User();
            user.setDocumento(request.getDocumento());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setTipoDocumento(request.getTipoDocumento());

            userRepository.save(user);

            UserRole role = new UserRole();
            role.setRoleName("USER");
            role.setUser(user);
            userRoleRepository.save(role);

            return "Usuario registrado con éxito";
        } catch (Exception e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            e.printStackTrace();
            return "Error al registrar el usuario";
        }
    }

    public void setTicketCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("ticket", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 día
        response.addCookie(cookie);
    }
}
