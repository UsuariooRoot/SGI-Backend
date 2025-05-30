package com.uoroot.sgi.infrastructure.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.exception.BusinessLogicException;
import com.uoroot.sgi.domain.exception.EmployeeNotFoundException;
import com.uoroot.sgi.domain.exception.UserNotFoundException;
import com.uoroot.sgi.domain.exception.UsernameAlreadyExistsException;
import com.uoroot.sgi.domain.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.uoroot.sgi.infrastructure.security.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import com.uoroot.sgi.infrastructure.api.dto.auth.request.LoginRequest;
import com.uoroot.sgi.infrastructure.api.dto.auth.request.RegisterRequest;
import com.uoroot.sgi.infrastructure.api.util.ResponseBuilder;
import com.uoroot.sgi.infrastructure.security.JwtUtil;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "API para la gestión de autenticación y autorización de usuarios")
public class AuthController {
    
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    @Operation(
        summary = "Iniciar sesión", 
        description = "Autentica a un usuario mediante nombre de usuario y contraseña, y devuelve un token JWT válido para acceder a los recursos protegidos",
        tags = {"Autenticación"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Autenticación exitosa", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"access_token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\", \"token_type\": \"Bearer\", \"expires_in\": 3600}"))
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Credenciales inválidas o usuario no encontrado", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"Credenciales inválidas\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"Error al autenticar\"}")
            )
        )
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtUtil.generateToken(authentication);
                
                Map<String, Object> response = new HashMap<>();
                response.put("access_token", jwt);
                response.put("token_type", "Bearer");
                response.put("expires_in", jwtUtil.getJwtExpiration() / 1000); // Convert from milliseconds to seconds
                
                return ResponseEntity.ok(response);
            } catch (BadCredentialsException|UsernameNotFoundException|UserNotFoundException e) {
                return ResponseBuilder.error(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
            } catch (AuthenticationException e) {
                logger.error("Error de autenticación: " + e.getMessage());
                return ResponseBuilder.error(HttpStatus.UNAUTHORIZED, "Error de autenticación");
            }
        } catch (Exception e) {
            logger.error("Error al autenticar: " + e.getMessage());
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al autenticar");
        }
    }

    @Operation(
        summary = "Registrar usuario", 
        description = "Registra un nuevo usuario en el sistema asociándolo a un empleado existente. Se requiere un nombre de usuario único, una contraseña segura y el ID del empleado.",
        tags = {"Autenticación"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuario registrado exitosamente", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"success\", \"message\": \"Usuario registrado exitosamente\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de registro inválidos o incompletos", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"La contraseña debe tener al menos 8 caracteres\"}")
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Empleado no encontrado con el ID proporcionado", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"No se encontró un empleado con el ID: 123\"}")
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "El nombre de usuario ya existe en el sistema", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"El nombre de usuario 'usuario1' ya está en uso\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"Error al registrar usuario\"}")
            )
        )
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            authService.registerUser(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmployeeId()
            );
            
            return ResponseBuilder.success("Usuario registrado exitosamente");
        } catch (EmployeeNotFoundException e) {
            return ResponseBuilder.error(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UsernameAlreadyExistsException e) {
            return ResponseBuilder.error(HttpStatus.CONFLICT, e.getMessage());
        } catch (BusinessLogicException e) {
            return ResponseBuilder.error(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar usuario: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Cerrar sesión", 
        description = "Cierra la sesión del usuario invalidando el token JWT actual. El token se agrega a una lista negra para evitar su uso posterior.",
        tags = {"Autenticación"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Sesión cerrada exitosamente", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"success\", \"message\": \"Sesión cerrada exitosamente\"}")
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Token inválido, expirado o no proporcionado en el encabezado Authorization", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"Token inválido o no proporcionado\"}")
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno del servidor", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class, example = "{\"status\": \"error\", \"message\": \"Error al cerrar sesión\"}")
            )
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String headerAuth = request.getHeader("Authorization");
            String token = null;
            
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                token = headerAuth.substring(7);
                
                // Verify that the token is valid before adding it to the blacklist
                if (jwtUtil.validateJwtToken(token)) {
                    // Add token to blacklist
                    tokenBlacklistService.blacklistToken(token);
                    
                    // Clear security context
                    SecurityContextHolder.clearContext();
                    
                    return ResponseBuilder.success("Sesión cerrada exitosamente");
                }
            }
            
            return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Token inválido o no proporcionado");
        } catch (Exception e) {
            logger.error("Error al cerrar sesión: " + e.getMessage());
            return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error al cerrar sesión");
        }
    }
}
