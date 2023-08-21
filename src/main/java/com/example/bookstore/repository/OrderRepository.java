package com.example.bookstore.repository;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.Order;
import com.example.bookstore.domain.OrderItem;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> getOrderByUserId(String id);
    @Aggregation(pipeline = {
            "{$unwind: '$books'}",
            "{$group: {_id: '$books', count: {$sum: 1}}}",
            "{$sort: {count: -1}}",
            "{$limit: 1}"
    })
    List<OrderItem> findMostCommonOrderItem();
}
