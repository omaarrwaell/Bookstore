package com.example.bookstore.services;

import com.example.bookstore.domain.OrderItem;
import com.example.bookstore.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service

public class OrderItemService {
   private OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem save(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    }
}
