package com.example.bookstore.services;

import com.example.bookstore.domain.Checkout;
import com.example.bookstore.repository.CheckoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckoutService {
    private CheckoutRepository checkoutRepository;
    @Autowired
    public CheckoutService(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }
    public Checkout  save(Checkout checkout){
        return checkoutRepository.save(checkout);
    }
    public Checkout getCheckout(String userid){
        return checkoutRepository.findByUserId(userid);
    }
}
