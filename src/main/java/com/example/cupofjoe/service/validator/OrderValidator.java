package com.example.cupofjoe.service.validator;

import com.example.cupofjoe.entity.Orders;

public interface OrderValidator {
    public Orders findByOrderByIdAndSellerId(Long orderId, String userId);
}
