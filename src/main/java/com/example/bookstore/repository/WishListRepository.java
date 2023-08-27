package com.example.bookstore.repository;

import com.example.bookstore.domain.WishList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends MongoRepository<WishList,String> {
    WishList findByUserId(String id);
}