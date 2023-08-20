package com.example.bookstore.services;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.OrderItem;
import com.example.bookstore.domain.ShoppingCart;
import com.example.bookstore.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService  {
    private final ShoppingCartRepository shoppingCartRepository;
    private  OrderItemService orderItemService;
//        @Autowired
//        public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
//                this.shoppingCartRepository = shoppingCartRepository;
//        }

@Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, OrderItemService orderItemService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.orderItemService = orderItemService;
    }

    public ShoppingCart save(ShoppingCart shoppingCart){
            return shoppingCartRepository.save(shoppingCart);
        }


        public List<OrderItem> getOrderItems(ShoppingCart cart){
            List<OrderItem> orderItems = new ArrayList<>();
            for(Book book : cart.getBooks()){
                OrderItem orderItem = new OrderItem(book.getName(),book.getPrice(),book);
                orderItems.add(orderItem);
                orderItemService.save(orderItem);
            }
            return orderItems;
        }

    public ShoppingCart GetByUserId(String id) {
    return shoppingCartRepository.findByUserId(id);
    }
}
