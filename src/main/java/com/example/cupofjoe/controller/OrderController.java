package com.example.cupofjoe.controller;

import com.example.cupofjoe.constant.route.Routes;
import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.UpdateResponse;
import com.example.cupofjoe.dto.order.AddOrderRequest;
import com.example.cupofjoe.dto.order.OrderResponse;
import com.example.cupofjoe.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(Routes.ADD_ORDER)
    public Message addOrder(@Valid @RequestBody AddOrderRequest request){
        log.info("Add order request :: {}", request);
        return orderService.addOrderRequest(request);
    }

    @GetMapping(Routes.ORDER_TO_ME)
    public List<OrderResponse> findMySales(@RequestParam(value = "orderStatus", required = false) String orderStatus){
        log.info("Get order based on status :: {}", orderStatus);
        return orderService.findMySales(orderStatus);
    }

    @GetMapping(Routes.ORDER_BY_ME)
    public List<OrderResponse> findMyBuys(@RequestParam(value = "orderStatus", required = false) String orderStatus){
        log.info("Get order based on status :: {}", orderStatus);
        return orderService.findMyBuys(orderStatus);
    }

    @PutMapping(Routes.MARK_AS_PREPARED)
    public UpdateResponse<OrderResponse> markOrderAsPrepared(@PathVariable("orderId") Long orderId){
        log.info("Mark order as prepared :: {}", orderId);
        return orderService.markAsPrepared(orderId);
    }

    @PutMapping(Routes.MARK_AS_COMPLETE)
    public UpdateResponse<OrderResponse> markOrderAsCompleted(@PathVariable("orderId") Long orderId){
        log.info("Mark order as prepared :: {}", orderId);
        return orderService.markAsCompleted(orderId);
    }
}
