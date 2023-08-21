package com.example.bookstore.controllers;

import com.example.bookstore.domain.*;
import com.example.bookstore.services.BookService;
import com.example.bookstore.services.ReviewService;
import com.example.bookstore.services.ShoppingCartService;
import com.example.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BookController {

    private BookService bookService;
    private UserService userService;
    private final ShoppingCartService shoppingCartService;
    private ReviewService reviewService;
    @Autowired
    public BookController(BookService bookService, UserService userService, ShoppingCartService shoppingCartService,ReviewService reviewService) {
        this.bookService = bookService;
        this.userService=userService;
        this.shoppingCartService = shoppingCartService;
        this.reviewService=reviewService;
    }

    @PostMapping("admin/books/create")
    public ResponseEntity<String> createBook(@RequestBody Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> validationErrors = bindingResult.getAllErrors();
            return ResponseEntity.badRequest().body(validationErrors.toString());
        } else {
            Book newBook = bookService.save(book);
            return ResponseEntity.ok("Book Created successfully !");
        }

    }

    @GetMapping("/books")
    public Page<Book> getBooks(@RequestParam int pageNo,@RequestParam int size,@RequestParam(value="sortBy", required = false) String sortBy)
    {
        Pageable page = PageRequest.of(pageNo,size, Sort.by(sortBy).descending());
        return bookService.findAllByPage(page);
    }

    @GetMapping("/books/{name}")
    public ResponseEntity<Book> getBook(@PathVariable String name) {
        Optional<Book> book = bookService.findBookByName(name);
        if (book.get() == null) {
            return ResponseEntity.of(book);
        } else {
            return ResponseEntity.ok(book.get());
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<Book>> search( @RequestParam(value = "query" ,required = false) String query,
                                              @RequestParam(value = "author", required = false) String author,
                                              @RequestParam(value = "category", required = false) String category,
                                              @RequestParam(value = "minPrice", required = false) Double minPrice,
                                              @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                                              @RequestParam(value = "startDate", required = false) Date startDate,
                                              @RequestParam(value = "endDate", required = false) Date endDate){
        List<Book> suggestions = bookService
                .getAutocompleteSuggestions(query, author, category, minPrice, maxPrice, startDate, endDate);
        return ResponseEntity.ok(suggestions);
    }


    @PostMapping("/books/{name}/add-to-cart")
    public ResponseEntity<String> addToCart(@PathVariable String name ,@RequestHeader String Authorization ){
        User currentUser =userService.getLoggedInUser(Authorization).get();

       // ShoppingCart shoppingCart = currentUser.getShoppingCart();
        ShoppingCart shoppingCart = shoppingCartService.GetByUserId(currentUser.getId());
        if(shoppingCart == null){
            shoppingCart= new ShoppingCart();
            shoppingCart.setUserId(currentUser);
        }

        shoppingCart.getBooks().add(bookService.findBookByName(name).get());
        shoppingCartService.save(shoppingCart);

        return ResponseEntity.ok(shoppingCart.toString());

        }


    @DeleteMapping("admin/books/{name}")
    public ResponseEntity<String> deleteBook(@PathVariable String name) {
        Optional<Book> book = bookService.findBookByName(name);
        if (book.get() == null) {
            return ResponseEntity.badRequest().body(name);
        } else {
            bookService.deleteBookByName(name);

            return ResponseEntity.ok("Book deleted");
        }
    }
    @PutMapping("admin/books/{bookId}")
    public ResponseEntity<String> updateBook(@PathVariable String bookId, @RequestBody Map<String, Object> updates) {
        if (!bookService.existsById(bookId)) {
            return ResponseEntity.notFound().build();
        }

        Book updatedBook = bookService.updateBook(bookId, updates);

        if (updatedBook != null) {
            return ResponseEntity.ok("Book updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the book");
        }
    }
//
    @PostMapping("/books/{bookName}/Review")
    public ResponseEntity<String> leaveReview(@PathVariable String bookName,@RequestBody Review review){
        Book book = bookService.findBookByName(bookName).get();
        review.setBookId(book);
        review = reviewService.save(review);

        return ResponseEntity.ok(review.toString());
    }

}