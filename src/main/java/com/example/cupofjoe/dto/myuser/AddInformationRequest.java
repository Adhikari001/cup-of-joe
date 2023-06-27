package com.example.cupofjoe.dto.myuser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"conformPassword"})
@EqualsAndHashCode(callSuper = false)
public class AddInformationRequest extends PasswordDTO{
    @NotBlank(message = "first name is empty")
    private String firstName;

    @NotBlank(message = "last name is empty")
    private String lastName;

    @NotBlank(message = "conform password can not be blank")
    private String conformPassword;

    private String cafeName;
}
