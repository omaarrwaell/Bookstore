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
    private String id;


    @DBRef
    @Field
    private List<Book> books = new ArrayList<>();

    @Field
    private double total;

    @DBRef
    private User userId;

    public double getTotal() {
        double total=0;
        for ( Book book:books
             ) {
            total+=book.getPrice();

        }
        return total;
    }
}





