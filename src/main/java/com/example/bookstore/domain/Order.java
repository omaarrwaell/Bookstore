package com.example.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document

@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    @Id

    private String id;
    @DBRef

    private User userId;
@Field
private String phoneNumber;
    @DBRef
    @Field

    private List<OrderItem> books = new ArrayList<>();

    @Field

    private double price;

    @Field

    private Date creation_date;

    @Field

    private String status;
    @Field

    private String address;

    @Field

    private boolean isPlaced =false;

    public Order(List<OrderItem> orderItems){
//        this.userId=userId;
        this.books=orderItems;
        this.isPlaced=false;
    }
    public double getTotal() {
        double total=0;
        for ( OrderItem book:books
        ) {
            total+=book.getPrice();

        }
        return total;
    }


}
