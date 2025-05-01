package com.example.cerberos.dto;

import com.example.cerberos.entities.TipoDocumento;

import lombok.Data;

@Data
public class LoginRequest {
    private String documento;
    private String password;
    private TipoDocumento tipoDocumento;
}
