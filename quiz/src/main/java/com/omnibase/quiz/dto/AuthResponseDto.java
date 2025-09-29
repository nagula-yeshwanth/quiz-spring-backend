package com.omnibase.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String role;
    
    public AuthResponseDto(String token, Long id, String username, String role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.role = role;
    }
}