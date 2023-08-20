package com.example.bookstore.domain;

import com.example.bookstore.services.BookService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookDto {
    private String name;
    private String author;
    private double price;
    private String imageUrl;
    private double avgRating;

    public BookDto(String name, String author, double price, String imageUrl) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
}
