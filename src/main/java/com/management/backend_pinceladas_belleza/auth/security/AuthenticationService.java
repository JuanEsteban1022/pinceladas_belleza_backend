package com.management.backend_pinceladas_belleza.auth.security;

import com.management.backend_pinceladas_belleza.auth.dto.AuthRequest;
import com.management.backend_pinceladas_belleza.auth.dto.RegisterDto;
import com.management.backend_pinceladas_belleza.auth.entity.Usuario;
import com.management.backend_pinceladas_belleza.auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario signup(RegisterDto input) {
        Usuario user = new Usuario();
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setStatus(1);
        user.setRole(input.getRole());

        return userRepository.save(user);
    }

    public Usuario authenticate(AuthRequest input) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return userRepository.findByUsername(input.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        } catch (AuthenticationException ex) {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
