package com.example.bookstore.services;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.BookDto;
import com.example.bookstore.domain.Review;
import com.example.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    public final BookRepository bookRepository;
    private  ReviewService reviewService;

    public BookService(BookRepository bookRepository, ReviewService reviewService) {
        this.bookRepository = bookRepository;
        this.reviewService = reviewService;
    }

    public Book save(Book book){
        return bookRepository.save(book);
    }
    public List<BookDto> findAll(){
        List<Book> books= bookRepository.findAll();
        List<BookDto> bookDtos = new ArrayList<>();
        for(Book book : books){
            BookDto bookDto =new BookDto(book.getName(),book.getAuthor(),book.getPrice(),book.getImageUrl());
            double rating = getRating(book);
            bookDto.setAvgRating(rating);
            bookDtos.add(bookDto);

        }
        return bookDtos;
    }
    public Optional<Book> findById(String id){
        return bookRepository.findById(id);

    }
    public Optional<Book> findBookByName(String name){
        return bookRepository.findBookByName(name);
    }

    public void deleteBookByName(String name) {
        bookRepository.deleteBookByName(name);
    }

    public boolean existsById(String bookId) {
        return bookRepository.existsById(bookId);
    }

    public Book updateBook(String bookId, Map<String, Object> updates) {
        Book existingBook = bookRepository.findById(bookId).orElse(null);

        if (existingBook != null) {
            applyUpdates(existingBook, updates);
            return bookRepository.save(existingBook);
        } else {
            return null; // Book with the given ID doesn't exist
        }
    }
    private void applyUpdates(Book book, Map<String, Object> updates) {
        if (updates.containsKey("name")) {
            book.setTitle((String) updates.get("name"));
        }
        if (updates.containsKey("author")) {
            book.setAuthor((String) updates.get("author"));
        }
        if (updates.containsKey("price")) {
            book.setPrice((Double) updates.get("price"));
        }
        if (updates.containsKey("imageUrl")) {
            book.setImageURL((String) updates.get("imageUrl"));
        }
        // Add more fields to update as needed
    }
//    public boolean existsByName(String name) {
//        return bookRepository.existsByName(name);
//    }
//
//    public Book updateBook(Book updatedBook) {
//        return bookRepository.updateBook(updatedBook);
//    }

    public double getRating(Book book){
        double avgRating =0;
        for (Review review: reviewService.getByBookId(book.getId())) {
            avgRating=(review.getRating()+avgRating)/reviewService.getByBookId(book.getId()).size();

        }
        return avgRating;
    }
    public Page<Book> findAllByPage(Pageable page){
        return bookRepository.findAll(page);

    }

    public List<Book> getAutocompleteSuggestions(String query, String author, String category, Double minPrice, Double maxPrice, Date startDate, Date endDate) {

       List<Book> suggestions = new ArrayList<>();
       if(query != null){
           suggestions = bookRepository.findBookByNameContains(query);
       } else if (author != null) {
           suggestions = bookRepository.findBookByAuthor(author);
           if(category != null){
               suggestions = bookRepository.findBookByAuthorAndGenre(author,category);
           }

       } else if (category != null) {
           suggestions = bookRepository.findBookByGenre(category);

       }else if (minPrice != null && maxPrice != null) {
           return bookRepository.findBookByPriceBetween(minPrice, maxPrice);
       } else if (startDate != null && endDate != null) {
           return bookRepository.findBookByPubDateBetween(startDate, endDate);
       }

        return  suggestions;
    }
}
