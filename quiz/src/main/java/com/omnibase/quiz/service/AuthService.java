package com.omnibase.quiz.service;

import com.omnibase.quiz.dto.AuthResponseDto;
import com.omnibase.quiz.dto.LoginRequestDto;
import com.omnibase.quiz.dto.RegisterRequestDto;
import com.omnibase.quiz.model.User;
import com.omnibase.quiz.repository.UserRepository;
import com.omnibase.quiz.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();
        
        user = userRepository.save(user);
        
        String token = jwtUtil.generateToken(user);
        
        return new AuthResponseDto(token, user.getId(), user.getUsername(), user.getRole().name());
    }
    
    public AuthResponseDto registerAdmin(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.ADMIN)  // Set role to ADMIN
                .build();
        
        user = userRepository.save(user);
        
        String token = jwtUtil.generateToken(user);
        
        return new AuthResponseDto(token, user.getId(), user.getUsername(), user.getRole().name());
    }
    
    public AuthResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));
        
        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);
        
        return new AuthResponseDto(token, user.getId(), user.getUsername(), user.getRole().name());
    }
}