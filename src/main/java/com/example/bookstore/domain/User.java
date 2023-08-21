package com.example.bookstore.domain;

import com.example.bookstore.domain.validator.PasswordsMatch;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Document
@PasswordsMatch
public class User implements UserDetails {
    @Id
    //@Indexed(unique = true)
    @Generated
    private String id;

    public String getId() {
        return id;
    }

    @NonNull
    @Field

    private String email;

    @NonNull
    @Field
    private String password;

    @Transient
    private String confirmPassword;

    @DBRef
   // @NonNull
    @Field
    private Role role;

    @NonNull
    @Field
    private String firstName;

    @NonNull
    @Field
    private String lastName;

//    @DBRef
//    @Field
//    private List<Order> orders = new ArrayList<>();

//    @DBRef
//    @Field
//    private ShoppingCart shoppingCart = new ShoppingCart();

//    public ShoppingCart getShoppingCart() {
//        return shoppingCart;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
