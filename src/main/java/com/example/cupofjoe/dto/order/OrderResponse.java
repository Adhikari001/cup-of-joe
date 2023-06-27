package com.example.cupofjoe.dto.order;

import lombok.Builder;

import java.util.List;

@Builder
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
