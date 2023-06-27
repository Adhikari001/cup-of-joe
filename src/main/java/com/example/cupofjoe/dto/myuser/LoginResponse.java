package com.example.cupofjoe.dto.myuser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String cafeName;
    private String token;
    private String message;
}
