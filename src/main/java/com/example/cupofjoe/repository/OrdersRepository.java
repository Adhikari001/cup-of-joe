package com.example.cupofjoe.repository;

import com.example.cupofjoe.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    @Query(value = "select order from Orders order where order.seller.id = :userId")
    List<Orders> findMySales(String userId);

    @Query(value = "select order from Orders order where order.seller.id = :userId and order.orderStatus = :orderStatus")
    List<Orders> findMySales(String userId, String orderStatus);

    @Query(value = "select order from Orders order where order.seller.id = :userId")
    List<Orders> findMyBuys(String userId);

    @Query(value = "select order from Orders order where order.seller.id = :userId and order.orderStatus = :orderStatus")
    List<Orders> findMyBuys(String userId, String orderStatus);

    @Query(value = "select order from Orders order where order.id = :orderId and order.seller.id = :userId")
    Optional<Orders> findByOrderByIdAndSellerId(Long orderId, String userId);
}