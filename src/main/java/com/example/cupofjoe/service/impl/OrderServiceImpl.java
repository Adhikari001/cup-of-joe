package com.example.cupofjoe.service.impl;

import com.example.cupofjoe.comms.context.ContextHolderService;
import com.example.cupofjoe.comms.exceptionhandler.RestException;
import com.example.cupofjoe.constant.enums.OrderStatus;
import com.example.cupofjoe.dto.Message;
import com.example.cupofjoe.dto.UpdateResponse;
import com.example.cupofjoe.dto.order.AddOrderRequest;
import com.example.cupofjoe.dto.order.OrderItemRequest;
import com.example.cupofjoe.dto.order.OrderResponse;
import com.example.cupofjoe.entity.OrderItem;
import com.example.cupofjoe.entity.Orders;
import com.example.cupofjoe.repository.OrdersRepository;

import com.example.cupofjoe.service.OrderItemService;
import com.example.cupofjoe.service.OrderService;
import com.example.cupofjoe.service.validator.MyUserValidator;
import com.example.cupofjoe.service.validator.OrderValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderItemService orderItemService;
    private final MyUserValidator myUserValidator;
    private final ContextHolderService contextHolderService;
    private final OrderValidator orderValidator;



    @Autowired
    public OrderServiceImpl(OrdersRepository ordersRepository, OrderItemService orderItemService, MyUserValidator myUserValidator, ContextHolderService contextHolderService, OrderValidator orderValidator) {
        this.ordersRepository = ordersRepository;
        this.orderItemService = orderItemService;
        this.myUserValidator = myUserValidator;
        this.contextHolderService = contextHolderService;
        this.orderValidator = orderValidator;
    }

    @Override
    @Transactional
    public Message addOrderRequest(AddOrderRequest request) {
        Orders orders =new Orders();
        orders.setSeller(myUserValidator.validateMyUser(request.getCafeId()));
        orders.setBuyer(myUserValidator.validateMyUser(contextHolderService.getContext().getUserId()));
        orders.setOrderStatus(OrderStatus.ORDERED.name());
        prepareToAddOrder(request);
        orders =ordersRepository.saveAndFlush(orders);
        orderItemService.addOrderItems(request.getItem(), orders);
        return Message.builder().message("orders added successfully").id(orders.getId()).build();
    }

    @Override
    public List<OrderResponse> findMySales(String orderStatus) {
        List<Orders> orders;
        if (orderStatus==null){
            orders = ordersRepository.findMySales(contextHolderService.getContext().getUserId());
        }else {
            orders = ordersRepository.findMySales(contextHolderService.getContext().getUserId(), orderStatus);
        }
        return prepareOrderResponse(orders);
    }

    @Override
    public List<OrderResponse> findMyBuys(String orderStatus) {
        List<Orders> orders;
        if (orderStatus==null){
            orders = ordersRepository.findMyBuys(contextHolderService.getContext().getUserId());
        }else {
            orders = ordersRepository.findMyBuys(contextHolderService.getContext().getUserId(), orderStatus);
        }
        return prepareOrderResponse(orders);
    }

    @Override
    public UpdateResponse<OrderResponse> markAsPrepared(Long orderId){
        Orders orders = orderValidator.findByOrderByIdAndSellerId(orderId, contextHolderService.getContext().getUserId());
        if(!orders.getOrderStatus().equalsIgnoreCase(OrderStatus.ORDERED.name())) throw new RestException("OS001", "Order status not ordered to mark it as prepared");
        orders.setOrderStatus(OrderStatus.PREPARED.name());
        ordersRepository.save(orders);
        return new UpdateResponse<>(prepareEachOrderResponse(orders), "Order updated successfully");
    }

    @Override
    public UpdateResponse<OrderResponse> markAsCompleted(Long orderId){
        Orders orders = orderValidator.findByOrderByIdAndSellerId(orderId, contextHolderService.getContext().getUserId());
        if(!orders.getOrderStatus().equalsIgnoreCase(OrderStatus.PREPARED.name())) throw new RestException("OS002", "Order status not prepared to mark it as completed");
        orders.setOrderStatus(OrderStatus.COMPLETED.name());
        ordersRepository.save(orders);
        return new UpdateResponse<>(prepareEachOrderResponse(orders), "Order updated successfully");
    }


    private List<OrderResponse> prepareOrderResponse(List<Orders> orders) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        orders.forEach(order->orderResponses.add(prepareEachOrderResponse(order)));
        return orderResponses;
    }

    private OrderResponse prepareEachOrderResponse(Orders order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .sellerFirstName(order.getSeller().getFirstName())
                .sellerLastName(order.getSeller().getLastName())
                .buyerFirstName(order.getBuyer().getFirstName())
                .buyerLastName(order.getBuyer().getLastName())
                .cafeName(order.getBuyer().getCafeName())
                .items(prepareOrderItemResponse(orderItemService.getItemsOfOrder(order)))
                .build();
    }

    private List<OrderItemRequest> prepareOrderItemResponse(List<OrderItem> orders) {
        List<OrderItemRequest> response = new ArrayList<>();
        orders.forEach(order->response.add(new OrderItemRequest(order.getItemName(), order.getOrderQuantity(), order.getAdditionalInformation())));
        return response;
    }

    private Orders prepareToAddOrder(AddOrderRequest request) {
        Orders orders = new Orders();

        return orders;
    }
}
