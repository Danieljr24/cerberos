package com.example.cerberos.dto;

import com.example.cerberos.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    public String token;
    public User user;
}
