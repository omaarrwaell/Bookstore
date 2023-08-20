package com.example.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document
@Getter

@RequiredArgsConstructor
public class Checkout {
    @Id
    @Generated
    private String id;
    @DBRef
    @Field
    private User userId;

    @DBRef
    @Field
    @NonNull
    private List<OrderItem> books = new ArrayList<>();
    @Field
    private double total=0;

    public double getTotal() {
        double total=0;
        for ( OrderItem book:books
        ) {
            total+=book.getPrice();

        }
        return total;
    }

    public Checkout(User user, List<OrderItem> orderItems) {
        this.userId=user;
        this.books=orderItems;
    }
}
