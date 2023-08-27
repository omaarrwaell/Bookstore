package com.example.bookstore.repository;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.Order;
import com.example.bookstore.domain.OrderItem;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order,String> {
    List<Order> getOrderByUserId(String id);
//    @Aggregation(pipeline = {
//            "{$unwind: '$books'}",
//            "{$group: {_id: '$books', count: {$sum: 1}}}",
//            "{$sort: {count: -1}}"
//
////            "{$limit: 1}"
//    })
//    List<OrderItem> findMostCommonOrderItem();
//@Aggregation(pipeline = {
//        "{$unwind: '$books'}",
//        "{$lookup: {from: 'orderItem', localField: 'books', foreignField: '_id', as: 'orderItem'}}",
//        "{$unwind: '$orderItem'}",
//        "{$lookup: {from: 'book', localField: 'orderItem.bookId', foreignField: '_id', as: 'book'}}",
//        "{$unwind: '$book'}",
//        "{$group: {_id: '$book.bookName', count: {$sum: 1}}}",
//        "{$sort: {count: -1}}"
//     //   "{$limit: 1}"
//})
//List<OrderItem> findMostCommonOrderItem();
@Aggregation(pipeline = {
        "{$unwind: '$books'}", // Unwind the list of order items
        "{$group: {_id: '$books', count: {$sum: 1}}}", // Group by bookId and count occurrences
        "{$sort: {count: -1}}", // Sort in descending order by count
        "{$limit: 1}" // Limit to the most common book
})
List<OrderItem> findMostCommonOrderItem();



    @Aggregation(pipeline = {
            "{$unwind: '$books'}",
            "{$lookup: {from: 'orderItem', localField: 'books', foreignField: '_id', as: 'orderItem'}}",
            "{$unwind: '$orderItem'}",
            "{$lookup: {from: 'book', localField: 'orderItem.bookId', foreignField: '_id', as: 'book'}}",
            "{$unwind: '$book'}",
            "{$group: {_id: '$book.genre', count: {$sum: 1}}}",
            "{$sort: {count: -1}}"
           // "{$limit: 1}"
    })
    List<String> findMostPopularGenre();


}
