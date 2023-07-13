package com.example.cupofjoe.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String orderStatus;
    private String sellerFirstName;
    private String sellerLastName;
    private String buyerFirstName;
    private String buyerLastName;
    private String cafeName;
    private List<OrderItemRequest> items;
}
