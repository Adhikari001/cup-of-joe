package com.example.cupofjoe.dto.myuser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends PasswordDTO {
    @NotBlank(message = "email is empty")
    @Email(message = "email is not valid")
    private String email;
}
