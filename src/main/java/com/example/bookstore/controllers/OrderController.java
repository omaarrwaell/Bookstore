package com.example.bookstore.controllers;

import com.example.bookstore.domain.Order;
import com.example.bookstore.domain.User;
import com.example.bookstore.services.MailService;
import com.example.bookstore.services.OrderService;
import com.example.bookstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;

    private  final MailService mailService;


     @GetMapping("/OrderHistory")
     public ResponseEntity<List<Order>> getAllOrders(@RequestHeader String Authorization) {
         User user = userService.getLoggedInUser(Authorization).orElse(null);
         if (user == null) {
             return ResponseEntity.badRequest().body(null); // Return appropriate response for unauthenticated user
         }

         List<Order> orders = orderService.getOrderByUserId(user.getId());
         return ResponseEntity.ok(orders);
     }

     @GetMapping("/OrderHistory/{orderId}")
     public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
         Order order = orderService.getOrderById(orderId);
         if (order == null) {
             return ResponseEntity.notFound().build(); // Return appropriate response if order not found
         }

         return ResponseEntity.ok(order);
     }

    @PutMapping("/admin/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@RequestParam String status, @PathVariable String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.notFound().build(); // Return appropriate response if order not found
        }

        User user = order.getUserId();
        order.setStatus(status);
        Order saved = orderService.save(order);
        mailService.sendEmailFromTemplate(user, "email/welcome", "Your Order is " + status);

        return ResponseEntity.ok(saved);
    }



}
