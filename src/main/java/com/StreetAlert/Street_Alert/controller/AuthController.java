package com.StreetAlert.Street_Alert.controller;

import com.StreetAlert.Street_Alert.dto.Request.LoginRequestDTO;
import com.StreetAlert.Street_Alert.dto.Request.RegisterRequestDTO;
import com.StreetAlert.Street_Alert.dto.Response.AuthResponseDTO;
import com.StreetAlert.Street_Alert.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authService.register(registerRequestDTO));
    }
}
