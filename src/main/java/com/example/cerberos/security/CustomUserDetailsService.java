package com.example.cerberos.security;

import com.example.cerberos.entities.User;
import com.example.cerberos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String documento) throws UsernameNotFoundException {
        User user = userRepository.findByDocumentoWithRoles(documento)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con documento: " + documento));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getDocumento())
                .password(user.getPassword())
                .authorities(user.getRoles().stream()
                        .map(role -> "ROLE_" + role.getRoleName())
                        .toArray(String[]::new))
                .build();
    }
}
