package com.example.bookstore.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Document
public class Role {

    @Id
    private String id;

    @NonNull
    @Field
    private String name;

    @DBRef
    @Field
    private Collection<User> users;
}
