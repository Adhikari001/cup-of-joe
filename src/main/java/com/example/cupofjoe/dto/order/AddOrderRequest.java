package com.example.cupofjoe.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
public class AddOrderRequest {
    @NotNull(message = "Cafe id can not be null")
    @NotEmpty(message = "Cafe id can not be empty")
    private String cafeId;
    @NotNull(message = "Order item can not be null")
    @NotEmpty(message = "Order items can not be empty")
    @Valid
    private List<OrderItemRequest> item;
}
