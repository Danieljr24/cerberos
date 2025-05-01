package com.example.cerberos.dto;

import lombok.Data;

import java.util.List;

import com.example.cerberos.entities.TipoDocumento;

@Data
public class RegisterRequest {
    private String documento;
    private String password;
    private TipoDocumento tipoDocumento;
    private List<UserRoleRequest> roles;

    @Data
    public static class UserRoleRequest {
        private String microservice;
        private String role;
    }
}
