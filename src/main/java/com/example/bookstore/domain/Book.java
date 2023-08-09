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

@Getter
@Setter
@ToString
public class Book {
    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String author;
    @Field
    private String genre;
    @Field
    private double price;
    @Field
    private Date pub_date;
    @Field
    @DBRef
    private List<Review> reviews =new ArrayList<>();

}
