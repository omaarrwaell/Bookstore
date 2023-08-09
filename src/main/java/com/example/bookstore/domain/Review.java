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
public class Review {
    @Id
    private String id ;

    @Field
    private String comment;
    @Field
    private double rating;

    @DBRef
    @NonNull
    private Book bookId;
}
