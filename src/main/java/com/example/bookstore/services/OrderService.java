package com.example.bookstore.services;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.Order;
import com.example.bookstore.domain.OrderItem;
import com.example.bookstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BookService bookService;
    public Order save(Order order){
        return orderRepository.save(order);
    }

    public List<Order> getOrderByUserId(String id) {

        return orderRepository.getOrderByUserId(id);
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).get();
    }

    public List<Map<String, Object>> findMostCommonOrderItem(int topCount) {
        Map<String, Integer> bookCounts = new HashMap<>();

        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            for (OrderItem orderItem : order.getBooks()) {
                String bookId = orderItem.getBookId().getId();
                bookCounts.put(bookId, bookCounts.getOrDefault(bookId, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedBookCounts = bookCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(topCount)
                .collect(Collectors.toList());

        List<Map<String, Object>> topBooks = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedBookCounts) {
            Book book = bookService.findById(entry.getKey()).orElse(null);
            if (book != null) {
                Map<String, Object> bookInfo = new HashMap<>();
                bookInfo.put("book", book);
                bookInfo.put("soldCount", entry.getValue());
                topBooks.add(bookInfo);
            }
        }

        return topBooks;
    }

    public List<Map.Entry<String, Integer>> findPopularGenres(int topCount){
        Map<String, Integer> genreCounts = new HashMap<>();

        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            for (OrderItem orderItem : order.getBooks()) {
                Book book = orderItem.getBookId();
                String genre = book.getGenre();
                genreCounts.put(genre, genreCounts.getOrDefault(genre, 0) + 1);
            }
        }

        List<Map.Entry<String, Integer>> sortedGenreCounts = genreCounts.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(topCount)
                .collect(Collectors.toList());

        return sortedGenreCounts;
    }
    }

