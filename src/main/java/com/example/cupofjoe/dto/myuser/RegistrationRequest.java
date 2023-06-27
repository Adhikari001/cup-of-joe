package com.example.cupofjoe.dto.myuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationRequest {
    @NotBlank(message = "email is empty")
    @Email(message = "email is not valid")
    private String email;
}
