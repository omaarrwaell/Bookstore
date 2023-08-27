package com.example.bookstore.controllers;

import com.example.bookstore.domain.*;
import com.example.bookstore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class CheckoutController {
    @Autowired
    private UserService userService;
    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private  MailService mailService;
    @Autowired
    private BookService bookService;

    @GetMapping("/checkout")
    public ResponseEntity<Checkout> getCheckout(@RequestHeader String Authorization) {
        User user = userService.getLoggedInUser(Authorization).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(null); // Return appropriate response for unauthenticated user
        }

        Checkout checkout = checkoutService.getCheckout(user.getId());
        return ResponseEntity.ok(checkout);
    }
    @PostMapping("/checkout/buy")
    public ResponseEntity<String> buy(@RequestParam("id") String id,
                                      @RequestBody CheckoutDto checkoutDto,
                                      @RequestHeader String Authorization) {
        User user = userService.getLoggedInUser(Authorization).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found"); // Return appropriate response for unauthenticated user
        }

        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build(); // Return appropriate response if order not found
        }

        order.setAddress(checkoutDto.getAddress());
        order.setPrice(order.getTotal());
        order.setCreation_date(new Date());
        order.setStatus("Processing");
        order.setPhoneNumber(checkoutDto.getPhoneNumber());
        order.setPlaced(true);
        orderService.save(order);

        for (OrderItem orderItem : order.getBooks()) {
            Book book = bookService.findById(orderItem.getBookId().getId()).orElse(null);
            if (book != null) {
                book.setQuantity(book.getQuantity() - 1);
                if (book.getQuantity() < 5) {
                    mailService.sendLowStockAlert(book);
                }
                bookService.save(book);
            }
        }

        mailService.sendEmailFromTemplate(user, "email/welcome", "Your Order is confirmed");

        return ResponseEntity.ok(order.toString());
    }

}
