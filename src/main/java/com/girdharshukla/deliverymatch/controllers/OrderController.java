package com.girdharshukla.deliverymatch.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.girdharshukla.deliverymatch.models.Order;
import com.girdharshukla.deliverymatch.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController{

    private final OrderService orderService;
    
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }
    
    public record AddOrderRequestDto(
        double latitude,
        double longitude
    ){}
    @PostMapping("/add")
    public Order addOrder(@RequestBody AddOrderRequestDto orderDto){
        return orderService.saveOrder(orderDto);
    }
}
