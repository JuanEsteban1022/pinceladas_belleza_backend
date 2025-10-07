package com.management.backend_pinceladas_belleza.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterDto {
    private String username;

    private String password;

    private String role;
}
