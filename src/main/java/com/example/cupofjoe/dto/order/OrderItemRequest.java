package com.example.cupofjoe.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    @NotNull(message = "Item name can not be null")
    private String itemName;
    @NotNull(message = "item count can not be empty")
    private Integer itemCount;
    private String additionalInformation;
}
