package com.example.cupofjoe.dto.myuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateOtp {
    @NotNull(message = "admin id can not be null")
    private String userId;
    @NotBlank(message = "otp can not be blank")
    private String otp;
}
