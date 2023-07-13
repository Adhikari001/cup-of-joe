package com.example.cupofjoe.service;

import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.UpdateResponse;
import com.example.cupofjoe.dto.order.AddOrderRequest;
import com.example.cupofjoe.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Message addOrderRequest(AddOrderRequest request);

    List<OrderResponse> findOrderToMe(String orderStatus);

    List<OrderResponse> findOrdersByMe(String orderStatus);

    UpdateResponse<OrderResponse> markAsPrepared(Long orderId);

    UpdateResponse<OrderResponse> markAsCompleted(Long orderId);
}
