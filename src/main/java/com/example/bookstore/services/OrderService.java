package com.example.bookstore.services;

import com.example.bookstore.domain.Order;
import com.example.bookstore.domain.OrderItem;
import com.example.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public Order save(Order order){
        return orderRepository.save(order);
    }

    public List<Order> getOrderByUserId(String id) {

        return orderRepository.getOrderByUserId(id);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).get();
    }

    public List<OrderItem> findMostCommonOrderItem() {
        return orderRepository.findMostCommonOrderItem();
    }
}
