package com.example.cupofjoe.service.impl;

import com.example.cupofjoe.dto.order.OrderItemRequest;
import com.example.cupofjoe.entity.OrderItem;
import com.example.cupofjoe.entity.Orders;
import com.example.cupofjoe.repository.OrderItemRepository;
import com.example.cupofjoe.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> addOrderItems(List<OrderItemRequest> items, Orders orders) {
        List<OrderItem> orderItems = new ArrayList<>();
        items.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemName(item.getItemName());
            orderItem.setOrderQuantity(item.getItemCount());
            orderItem.setAdditionalInformation(item.getAdditionalInformation());
            orderItem.setOrders(orders);
        });
        return orderItemRepository.saveAllAndFlush(orderItems);
    }

    @Override
    public List<OrderItem> getItemsOfOrder(Orders order) {
        return orderItemRepository.findOrderItemsOfOrder(order);
    }
}
