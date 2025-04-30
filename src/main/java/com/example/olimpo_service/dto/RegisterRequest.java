package com.example.olimpo_service.dto;

import com.example.olimpo_service.entities.TipoDocumento;
import lombok.Data;

import java.util.List;

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
