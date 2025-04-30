package com.example.olimpo_service.dto;

import com.example.olimpo_service.entities.TipoDocumento;
import lombok.Data;

@Data
public class LoginRequest {
    private String documento;
    private String password;
    private TipoDocumento tipoDocumento;
}
