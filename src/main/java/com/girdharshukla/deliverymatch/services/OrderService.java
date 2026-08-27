package com.girdharshukla.deliverymatch.services;

import org.springframework.stereotype.Service;

import com.girdharshukla.deliverymatch.models.Order;
import com.girdharshukla.deliverymatch.repositories.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }


}
