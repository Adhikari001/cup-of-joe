package com.example.cupofjoe.service;

import com.example.cupofjoe.dto.order.OrderItemRequest;
import com.example.cupofjoe.entity.OrderItem;
import com.example.cupofjoe.entity.Orders;

import java.util.List;
import java.util.Set;

public interface OrderItemService {
    Set<OrderItem> addOrderItems(List<OrderItemRequest> item, Orders orders);

//    List<OrderItem> getItemsOfOrder(Orders order);
}
