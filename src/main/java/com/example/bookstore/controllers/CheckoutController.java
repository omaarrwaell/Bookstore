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
    public Checkout  getCheckout(@RequestHeader String Authorization){
        User user = userService.getLoggedInUser(Authorization).get();
        return checkoutService.getCheckout(user.getId());
    }
    @PostMapping("/checkout/buy")
    public ResponseEntity<String> buy(@RequestParam( "id") String id, @RequestBody CheckoutDto checkoutDto, @RequestHeader String Authorization) {
        User user = userService.getLoggedInUser(Authorization).get();
       Order order= orderService.getOrderById(id);
       // Order order = user.getOrders().stream().filter(o->o.getId().equals(id)).findFirst().get();

        order.setAddress(checkoutDto.getAddress());
        order.setPrice(order.getTotal());
        order.setCreation_date(new Date());
        order.setStatus("Processing");
        order.setPhoneNumber(checkoutDto.getPhoneNumber());
        order.setPlaced(true);
        orderService.save(order);
        for (OrderItem orderitem : order.getBooks()){
            Book book = bookService.findById(orderitem.getBookId().getId()).get();
            book.setQuantity(book.getQuantity()-1);
            if(book.getQuantity()<5){
                mailService.sendLowStockAlert(book);
            }
            bookService.save(book);
        }
        mailService.sendEmailFromTemplate(user, "email/welcome", "Your Order is confirmed");
        //user.getOrders().stream().filter(o->o.getId().);
      //  userService.save(user);

        return ResponseEntity.ok(order.toString());


    }

}
