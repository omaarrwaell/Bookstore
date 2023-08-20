package com.example.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@NoArgsConstructor

@Getter
@Setter
@ToString
public class OrderItem {

    @Id
    private String id ;
    @Field
    private String bookName;
    @Field
    private double price;

    @Field
    @DBRef
    private Book bookId;

    public OrderItem(String name, double price, Book bookId) {
        this.bookId=bookId;
        this.price=price;
        this.bookName=name;
    }
}
