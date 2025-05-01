package com.example.cerberos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.cerberos.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.documento = :documento")
    Optional<User> findByDocumentoWithRoles(@Param("documento") String documento);

    Optional<User> findByDocumento(String documento); // Cambiado de findByUsername a findByDocumento
}