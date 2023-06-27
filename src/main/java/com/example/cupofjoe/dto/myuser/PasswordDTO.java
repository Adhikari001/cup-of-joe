package com.example.cupofjoe.dto.myuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@ToString(exclude = {"password"})
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PasswordDTO {
    @NotBlank(message = "password is empty")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()]).{8,32}$",
            message = "password must contain a number, uppercase, lowercase,  and special character must occur once and must be 8 to 32 character long")
    private String password;
}
