package com.example.cupofjoe.dto.myuser;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {
    private String message;
    private String otp;
    private String id;
}
