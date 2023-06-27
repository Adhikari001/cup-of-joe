package com.example.cupofjoe.dto.myuser;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateOtpResponse {
    private String userId;
    private String message;
    private String token;
}
