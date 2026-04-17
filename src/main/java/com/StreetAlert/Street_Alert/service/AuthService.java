package com.StreetAlert.Street_Alert.service;

import com.StreetAlert.Street_Alert.dto.Request.LoginRequestDTO;
import com.StreetAlert.Street_Alert.dto.Request.RegisterRequestDTO;
import com.StreetAlert.Street_Alert.dto.Response.AuthResponseDTO;
import com.StreetAlert.Street_Alert.entity.User;
import com.StreetAlert.Street_Alert.repository.UserRepository;
import com.StreetAlert.Street_Alert.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in repository"));

        String token = jwtUtil.generateToken(principal);
        return new AuthResponseDTO(token, "Bearer", user.getId(), user.getUsername(), user.getRole());
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .email(registerRequestDTO.getEmail())
                .username(registerRequestDTO.getUsername())
                .passwordHash(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .role(User.Role.USER)
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);

        UserDetails principal = org.springframework.security.core.userdetails.User
                .withUsername(savedUser.getUsername())
                .password(savedUser.getPasswordHash())
                .authorities("ROLE_" + savedUser.getRole().name())
                .build();

        String token = jwtUtil.generateToken(principal);
        return new AuthResponseDTO(token, "Bearer", savedUser.getId(), savedUser.getUsername(), savedUser.getRole());
    }
}
