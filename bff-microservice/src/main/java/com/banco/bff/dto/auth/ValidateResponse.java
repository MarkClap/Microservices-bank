package com.banco.bff.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateResponse {
    private Boolean valid;
    private String username;
    private String message;
}