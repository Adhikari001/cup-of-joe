package com.example.cupofjoe.repository;

import com.example.cupofjoe.entity.OrderItem;
import com.example.cupofjoe.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query(value = "select item from OrderItem item where item.orders = :order")
    List<OrderItem> findOrderItemsOfOrder(Orders order);
}