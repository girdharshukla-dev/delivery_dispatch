package com.girdharshukla.deliverymatch.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.girdharshukla.deliverymatch.controllers.OrderController.AddOrderRequestDto;
import com.girdharshukla.deliverymatch.models.Order;
import com.girdharshukla.deliverymatch.repositories.OrderRepository;
import com.uber.h3core.H3Core;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final H3Core h3Core;

    @Value("${h3.resolution}") int resolution;

    public OrderService(OrderRepository orderRepository, H3Core h3Core){
        this.orderRepository = orderRepository;
        this.h3Core = h3Core;
    }

    public Order saveOrder(AddOrderRequestDto orderDto){
        Order order = new Order();
        
        order.setId(UUID.randomUUID());
        order.setLatitude(orderDto.latitude());
        order.setLongitude(orderDto.longitude());
        order.setH3Cell(h3Core.latLngToCell(orderDto.latitude(), orderDto.longitude(), resolution));
        order.setCreatedAt(LocalDateTime.now());
        
        return orderRepository.save(order);
    }


}
