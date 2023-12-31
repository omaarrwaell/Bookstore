package com.example.bookstore.controllers;

import com.example.bookstore.domain.*;
import com.example.bookstore.services.CheckoutService;
import com.example.bookstore.services.OrderService;
import com.example.bookstore.services.ShoppingCartService;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    private UserService userService;
    private ShoppingCartService shoppingCartService;

private OrderService orderService;

    @Autowired
    public CartController(UserService userService,ShoppingCartService shoppingCartService,OrderService orderService) {
        this.userService = userService;
        this.shoppingCartService=shoppingCartService;
        this.orderService=orderService;
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCart(@RequestHeader String Authorization) {
        User user = userService.getLoggedInUser(Authorization).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("user not authenticated"); // Return appropriate response for unauthenticated user
        }
        ShoppingCart cart = shoppingCartService.GetByUserId(user.getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/cart/checkout")
    public ResponseEntity<String> proceedCheckout(@RequestBody Order order, @RequestHeader String Authorization) {
        User user = userService.getLoggedInUser(Authorization).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found"); // Return appropriate response for unauthenticated user
        }

        List<OrderItem> orderItems = shoppingCartService.getOrderItems(shoppingCartService.GetByUserId(user.getId()));
        order.setBooks(orderItems);
        order.setUserId(user);

        Order savedOrder = orderService.save(order);
        return ResponseEntity.ok("/checkout/buy?id=" + savedOrder.getId());
    }
}
