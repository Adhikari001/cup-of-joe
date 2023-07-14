package com.example.cupofjoe.dto.myuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String cafeName;
    private String token;
    private String message;
}
