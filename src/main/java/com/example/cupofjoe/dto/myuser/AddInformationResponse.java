package com.example.cupofjoe.dto.myuser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddInformationResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String cafeName;
    private String message;
}
