package com.example.bookstore.repository;

import com.example.bookstore.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    Order getOrderByUserId(String id);
}
