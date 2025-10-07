package com.management.backend_pinceladas_belleza.auth.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

