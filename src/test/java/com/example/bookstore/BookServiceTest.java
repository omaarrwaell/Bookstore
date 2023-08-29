package com.example.bookstore;

import com.example.bookstore.domain.Book;
import com.example.bookstore.domain.ShoppingCart;
import com.example.bookstore.domain.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.services.BookService;
import com.example.bookstore.services.ShoppingCartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    @Autowired
    private ShoppingCartService shoppingCartService;

    @Test
    public void BookCreated(){
        Book book= new Book();
        when(bookRepository.save(book)).thenReturn(book);
        assertEquals(book,bookService.save(book));
    }
    @Test
    public void BookRead(){
        Book book = new Book();
        book.setName("oliver twist");
        when(bookRepository.findBookByName("oliver twist")).thenReturn(Optional.of(book));
        assertEquals(book, bookService.findBookByName("oliver twist").get());
    }

    @Test
    public void BookDelete(){
        Book book = new Book();
        book.setTitle("oliver twist");
        bookService.deleteBookByName("oliver twist");
        verify( bookRepository,times(1)).deleteBookByName("oliver twist");
    }

//    @Test
//    public void BookUpdate(){
//        Book book = new Book();
//        book.setId("1");
//        when(bookRepository.save(b))
//    }

//    @Test
//    public void AddtoCartTest(){
//        User user = new User();
//        user.setId("1");
//        ShoppingCart shoppingCart = new ShoppingCart();
//        shoppingCart.setUserId(user);
//        Book book = new Book();
//        book.setId("1");
//        shoppingCart.getBooks().add(book);
//
//       when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart );
//        ShoppingCart saved= shoppingCartService.addToCart(Optional.of(book), shoppingCart);
//       assertEquals(shoppingCart, saved);
//
//    }
}
