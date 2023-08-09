package com.example.bookstore.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Document
public class ShoppingCart {



    @Id
    private String id ;

    @DBRef
    @Field
    private User userId;

    @DBRef
    @Field
    private List<Book> books= new ArrayList<>();


}
