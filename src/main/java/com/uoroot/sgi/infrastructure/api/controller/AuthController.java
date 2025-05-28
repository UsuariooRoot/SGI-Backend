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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uoroot.sgi.domain.exception.BusinessLogicException;
import com.uoroot.sgi.domain.exception.EmployeeNotFoundException;
import com.uoroot.sgi.domain.exception.UserNotFoundException;
import com.uoroot.sgi.domain.exception.UsernameAlreadyExistsException;
import com.uoroot.sgi.domain.service.AuthService;
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
@CrossOrigin(origins = {"https://tu-dominio-angular.com", "http://localhost:4200"})
@RequiredArgsConstructor
public class AuthController {
    
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String headerAuth = request.getHeader("Authorization");
            String token = null;
            
            if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
                token = headerAuth.substring(7);
                
                // Verificar que el token sea válido antes de agregarlo a la lista negra
                if (jwtUtil.validateJwtToken(token)) {
                    // Invalidar el token agregándolo a la lista negra
                    tokenBlacklistService.blacklistToken(token);
                    
                    // Limpiar el contexto de seguridad
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
