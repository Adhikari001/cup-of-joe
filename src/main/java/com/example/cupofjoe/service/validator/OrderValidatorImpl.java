package com.example.cupofjoe.service.validator;

import com.example.cupofjoe.comms.exceptionhandler.RestException;
import com.example.cupofjoe.entity.Orders;
import com.example.cupofjoe.repository.OrdersRepository;

import org.springframework.stereotype.Service;

@Service
public class OrderValidatorImpl implements OrderValidator {
    private final OrdersRepository ordersRepository;

    public OrderValidatorImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    public Orders findByOrderByIdAndSellerId(Long orderId, String userId) {

        return ordersRepository.findByOrderByIdAndSellerId(orderId, userId)
                .orElseThrow(()->new RestException("OV001", "Can not validate order by order id and user id."));
    }
}
