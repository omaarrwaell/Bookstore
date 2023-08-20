package com.example.bookstore.repository;

import com.example.bookstore.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book,String> {

    public Optional<Book> findBookByName(String name);

    void deleteBookByName(String name);

    List<Book> findBookByNameContains(String query);

    List<Book> findBookByAuthor(String author);

    List<Book> findBookByAuthorAndGenre(String author, String genre);

    List<Book> findBookByGenre(String category);

    List<Book> findBookByPriceBetween(Double minPrice, Double maxPrice);

    List<Book> findBookByPubDateBetween(Date startDate, Date endDate);

    //  List<Book> findBooksByFilters(String query, String author, String category, Double minPrice, Double maxPrice, Date startDate, Date endDate);


//    boolean existsByName(String name);
//
//    Book updateBook(Book updatedBook);
}
