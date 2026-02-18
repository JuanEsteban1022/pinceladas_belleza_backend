package com.management.backend_pinceladas_belleza.auth.controller;

import com.management.backend_pinceladas_belleza.auth.dto.AuthRequest;
import com.management.backend_pinceladas_belleza.auth.dto.AuthResponse;
import com.management.backend_pinceladas_belleza.auth.dto.RegisterDto;
import com.management.backend_pinceladas_belleza.auth.entity.Usuario;
import com.management.backend_pinceladas_belleza.auth.security.AuthenticationService;
import com.management.backend_pinceladas_belleza.config.jwt.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para registro y autenticación de usuarios")
public class AuthController {
        private final JwtService jwtService;
        private final AuthenticationService authenticationService;

        @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
                        @ApiResponse(responseCode = "400", description = "Datos de registro inválidos", content = @Content)
        })
        @PostMapping("/signup")
        public ResponseEntity<Usuario> register(@RequestBody RegisterDto registerUserDto) {
                Usuario registeredUser = authenticationService.signup(registerUserDto);
                return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        }

        @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y devuelve un token JWT")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
        })
        @PostMapping("/login")
        public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest loginUserDto) {
                Usuario authenticatedUser = authenticationService.authenticate(loginUserDto);

                String jwtToken = jwtService.generateToken(authenticatedUser);

                AuthResponse loginResponse = new AuthResponse();
                loginResponse.setToken(jwtToken);
                loginResponse.setExpiresIn(jwtService.getExpirationTime());
                loginResponse.setUsuario(authenticatedUser);

                return ResponseEntity.ok(loginResponse);
        }
}
