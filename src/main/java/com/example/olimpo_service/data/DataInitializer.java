package com.example.olimpo_service.data;

import com.example.olimpo_service.entities.User;
import com.example.olimpo_service.entities.UserRole;
import com.example.olimpo_service.repository.UserRepository;
import com.example.olimpo_service.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔒 Para cifrar contraseñas

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User zeus = createUser("zeus", "12345");
            User dani = createUser("dani", "1234");

            createRole(zeus, "ADMIN");
            createRole(dani, "USER");

            System.out.println("Usuarios y roles creados correctamente.");
        }
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 🔐 Encryptar contraseña
        user.setRoles(new ArrayList<>());
        return userRepository.save(user);
    }

    private void createRole(User user, String roleName) {
        UserRole userRole = new UserRole();
        userRole.setRoleName(roleName);
        userRole.setUser(user);

        user.getRoles().add(userRole); // Asocia el rol al usuario

        userRoleRepository.save(userRole);
    }
}
