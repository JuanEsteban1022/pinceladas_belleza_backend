package com.management.backend_pinceladas_belleza.auth.dto;

import com.management.backend_pinceladas_belleza.auth.entity.Usuario;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private long expiresIn;
    private Usuario usuario;
}
