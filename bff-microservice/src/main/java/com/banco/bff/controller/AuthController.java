package com.banco.bff.controller;

import com.banco.bff.dto.auth.LoginRequest;
import com.banco.bff.dto.auth.LoginResponse;
import com.banco.bff.dto.auth.ValidateResponse;
import com.banco.bff.dto.auth.ErrorResponse;
import com.banco.bff.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            log.warn("Login fallido: username vacío");
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("error", "Username requerido"));
        }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            log.warn("Login fallido: password vacío para usuario: {}", request.getUsername());
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("error", "Password requerido"));
        }

        try {
            String token = jwtUtil.generateToken(request.getUsername());

            LoginResponse response = new LoginResponse(
                    token,
                    "Bearer",
                    86400L,
                    request.getUsername()
            );

            log.info("Login exitoso para usuario: {}", request.getUsername());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al generar token para usuario: {}", request.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("error", "Error al generar token"));
        }
    }
}