package com.pm.authservice.Services;

import com.pm.authservice.DTO.LoginRequestDTO;
import com.pm.authservice.Modal.User;
import com.pm.authservice.Repository.userRepository;
import com.pm.authservice.utils.JwtUtils;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final userRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    public AuthService(userRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public Optional<String> generateToken(LoginRequestDTO requestDTO) {
        Optional<User> usr = userRepository.findByEmail(requestDTO.getEmail());
        String email = requestDTO.getEmail();
        boolean val = passwordEncoder.matches(requestDTO.getPassword(), usr.get().getPassword());

        Optional<String> token = userRepository.findByEmail(requestDTO.getEmail())
                .filter(u-> passwordEncoder.matches(requestDTO.getPassword(),u.getPassword()))
                .map(u-> jwtUtils.generateToken(u.getEmail(),u.getRole()));
         return token;
    }

    public boolean validateToken(String token) {
        try {
            jwtUtils.validateToken(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }
}
