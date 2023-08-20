package com.example.bookstore.repository;

import com.example.bookstore.domain.ShoppingCart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends MongoRepository<ShoppingCart,String> {
    ShoppingCart findByUserId(String id);
}
