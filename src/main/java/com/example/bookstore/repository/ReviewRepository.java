package com.example.bookstore.repository;

import com.example.bookstore.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review,String> {
    List<Review> findByBookId(String id);
}
