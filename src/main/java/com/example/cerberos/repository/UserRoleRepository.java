package com.example.cerberos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cerberos.entities.User;
import com.example.cerberos.entities.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(User user);
    Optional<UserRole> findByRoleName(String roleName);  
}
