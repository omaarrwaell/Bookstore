package com.example.bookstore.services;

import com.example.bookstore.domain.Review;
import com.example.bookstore.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(Review review){
        return  reviewRepository.save(review);
    }

    public List<Review> getByBookId(String id) {
        return reviewRepository.findByBookId(id);
    }
}
