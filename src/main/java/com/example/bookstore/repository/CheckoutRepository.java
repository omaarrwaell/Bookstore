package com.example.bookstore.repository;

import com.example.bookstore.domain.Checkout;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CheckoutRepository extends MongoRepository<Checkout,String> {
    Checkout findByUserId(String userid);
}
