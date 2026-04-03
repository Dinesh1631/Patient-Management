package com.pm.authservice.Controller;

import com.pm.authservice.DTO.LoginRequestDTO;
import com.pm.authservice.DTO.LoginResponceDTO;
import com.pm.authservice.Services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController

public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponceDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO)
    {
        Optional<String> token = authService.generateToken(loginRequestDTO);
        if (token.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(new LoginResponceDTO(token.get()));
        }
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(
            @RequestHeader("Authorization") String authHeader) {

        // Authorization: Bearer <token>
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
