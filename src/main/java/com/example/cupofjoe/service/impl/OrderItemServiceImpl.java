package com.example.cupofjoe.service.impl;

import com.example.cupofjoe.dto.order.OrderItemRequest;
import com.example.cupofjoe.entity.OrderItem;
import com.example.cupofjoe.entity.Orders;
import com.example.cupofjoe.repository.OrderItemRepository;
import com.example.cupofjoe.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public Set<OrderItem> addOrderItems(List<OrderItemRequest> items, Orders orders) {
        Set<OrderItem> orderItems = new HashSet<>();
        items.forEach(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemName(item.getItemName());
            orderItem.setOrderQuantity(item.getItemCount());
            orderItem.setAdditionalInformation(item.getAdditionalInformation());
            orderItem.setOrders(orders);
            orderItemRepository.save(orderItem);
        });
        return orderItems;
    }

//    @Override
//    public List<OrderItem> getItemsOfOrder(Orders order) {
//        return orderItemRepository.findOrderItemsOfOrder(order);
//    }
}
