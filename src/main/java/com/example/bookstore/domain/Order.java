package com.example.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
public class Order {
    @Id
    private String id;
    @DBRef
    @Field
    @NonNull
    private User userId;

    @DBRef
    @Field
    @NonNull
    private List<OrderItem> books = new ArrayList<>();

    @Field
    @NonNull
    private double price;

    @Field
    @NonNull
    private Date creation_date;

    @Field
    @NonNull
    private String status;
    @Field
    @NonNull
    private String address;

}
