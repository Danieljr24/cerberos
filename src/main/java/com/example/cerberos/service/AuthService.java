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

import java.util.Optional;
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

    public User getUser(String documento) {
        return userRepository.findByDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
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
            e.printStackTrace();
            return "Error al registrar el usuario";
        }
    }

    public void setTicketCookie(String token, HttpServletResponse response) {
        // Crear y agregar la cookie con el token
        Cookie cookie = new Cookie("ticket", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // Cambiar a true si usas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);  // 1 día
        response.addCookie(cookie);
    }

    public void clearTicketCookie(HttpServletResponse response) {
        // Limpiar la cookie (eliminarla)
        Cookie cookie = new Cookie("ticket", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // Cambiar a true si usas HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public boolean hasRole(String documento, String roleName) {
        return userRepository.findByDocumentoWithRoles(documento)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getRoleName().equalsIgnoreCase(roleName)))
                .orElse(false);
    }

    public String getUserRole(String documento) {
        Optional<User> userOptional = userRepository.findByDocumentoWithRoles(documento);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Suponiendo que quieres obtener el primer rol asociado al usuario
            return user.getRoles().stream()
                    .map(UserRole::getRoleName)
                    .findFirst()
                    .orElse("No tiene roles asignados");
        } else {
            throw new RuntimeException("Usuario no encontrado");
        }
    }
}
