package com.example.cerberos.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cerberos.dto.LoginRequest;
import com.example.cerberos.dto.RegisterRequest;
import com.example.cerberos.entities.User;
import com.example.cerberos.entities.UserRole;
import com.example.cerberos.repository.UserRepository;
import com.example.cerberos.repository.UserRoleRepository;
import com.example.cerberos.util.JwtUtil;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       UserRoleRepository userRoleRepository,
                       JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByDocumentoWithRoles(request.getDocumento())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        var roles = user.getRoles().stream()
                .map(UserRole::getRoleName)
                .collect(Collectors.toList());

        return jwtUtil.generateToken(user.getDocumento(), roles);
    }

    public String register(RegisterRequest request) {
        try {

            if (userRepository.findByDocumento(request.getDocumento()).isPresent()) {
                return "El usuario ya existe";
            }

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
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }
}
