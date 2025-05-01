package com.example.cerberos.data;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.cerberos.entities.TipoDocumento;
import com.example.cerberos.entities.User;
import com.example.cerberos.entities.UserRole;
import com.example.cerberos.repository.UserRepository;
import com.example.cerberos.repository.UserRoleRepository;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initializeData() {
        if (userRepository.count() == 0) {
            User zeus = createUser("10000001", "12345", TipoDocumento.CEDULA_CIUDADANIA);
            User dani = createUser("10000002", "1234", TipoDocumento.TARJETA_IDENTIDAD);

            createRole(zeus, "ADMIN");
            createRole(dani, "USER");

            System.out.println("Usuarios y roles creados correctamente.");
        }
    }

    private User createUser(String documento, String password, TipoDocumento tipoDocumento) {
        User user = new User();
        user.setDocumento(documento);
        user.setPassword(passwordEncoder.encode(password));
        user.setTipoDocumento(tipoDocumento);
        user.setRoles(new ArrayList<>());
        return userRepository.save(user);
    }

    private void createRole(User user, String roleName) {
        UserRole userRole = new UserRole();
        userRole.setRoleName(roleName);
        userRole.setUser(user);
        user.getRoles().add(userRole);
        userRoleRepository.save(userRole);
    }
}
