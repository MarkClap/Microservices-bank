package com.banco.bff.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String access_token;
    private String token_type;
    private Long expires_in;
    private String username;
}