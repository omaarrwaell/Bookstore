package com.example.bookstore.services;

import com.example.bookstore.domain.WishList;
import com.example.bookstore.repository.WishListRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;

    public WishList save(WishList wishList){
        return wishListRepository.save(wishList);
    }

    public WishList GetByUserId(String id) {
        return wishListRepository.findByUserId(id);
    }
}