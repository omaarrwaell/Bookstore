
package com.example.bookstore.controllers;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.ShoppingCart;
import com.example.bookstore.domain.validator.PasswordsMatch;
import com.example.bookstore.services.BookService;

import com.example.bookstore.domain.User;
import com.example.bookstore.domain.WishList;
import com.example.bookstore.services.ShoppingCartService;
import com.example.bookstore.services.UserService;
import com.example.bookstore.services.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class WishListController {

    private final UserService userService;
    private final ShoppingCartService shoppingCartService;
    private final WishListService wishListService;
    private final BookService bookService;


    @GetMapping("/WishList")
    public ResponseEntity<WishList> getWishList(@RequestHeader String Authorization){
        User user = userService.getLoggedInUser(Authorization).get();
        WishList wishList = wishListService.GetByUserId(user.getId());
        if(wishList!= null){
            return ResponseEntity.ok(wishList);
        }else{
            return ResponseEntity.badRequest().body(wishList);
        }
    }

    @PostMapping("/WishList/{bookName}/add-to-cart")
    public ResponseEntity<ShoppingCart>  addWishListToCart(@PathVariable String bookName, @RequestHeader String Authorization){
        User user = userService.getLoggedInUser(Authorization).get();
        WishList wishList = wishListService.GetByUserId(user.getId());

        ShoppingCart shoppingCart = shoppingCartService.GetByUserId(user.getId());
        Optional<Book> optionalBook = bookService.findBookByName(bookName);
        if(optionalBook.get()== null){
            return ResponseEntity.badRequest().body(shoppingCart);
        }else{
            shoppingCart.getBooks().add(optionalBook.get());
            wishList.getBooks().remove(optionalBook.get());
            wishListService.save(wishList);
            shoppingCartService.save(shoppingCart);
            return ResponseEntity.ok(shoppingCart);
        }

    }

}