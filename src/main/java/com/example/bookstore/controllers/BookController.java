package com.example.bookstore.controllers;

import com.example.bookstore.domain.*;
import com.example.bookstore.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private BookService bookService;
    private UserService userService;
    private final ShoppingCartService shoppingCartService;
    private ReviewService reviewService;
    private BrowsingHistoryService browsingHistoryService;
    private WishListService wishListService;
    @Autowired
    public BookController(BookService bookService, UserService userService,
                          ShoppingCartService shoppingCartService,ReviewService reviewService,
                          BrowsingHistoryService browsingHistoryService, WishListService wishListService) {
        this.bookService = bookService;
        this.userService=userService;
        this.shoppingCartService = shoppingCartService;
        this.reviewService=reviewService;
        this.browsingHistoryService=browsingHistoryService;
        this.wishListService=wishListService;
    }

    @PostMapping("admin/books/create")
    public ResponseEntity<String> createBook(@RequestBody @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            String errorMessage = fieldErrors.stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            Book newBook = bookService.save(book);
            return ResponseEntity.ok("Book Created successfully !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while creating the book: " + e.getMessage());
        }
    }


    @GetMapping("/books")
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam int pageNo,
            @RequestParam int size,
            @RequestParam(value = "sortBy", required = false) String sortBy) {

        try {
            Pageable page = PageRequest.of(pageNo, size, Sort.by(sortBy).descending());
            Page<Book> books = bookService.findAllByPage(page);

            for (Book book : books.getContent()) {
                book.setRating(bookService.getRating(book));
                bookService.save(book);
            }

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @GetMapping("/books/{name}")
    public ResponseEntity<Book> getBook(
            @PathVariable String name,
            @RequestHeader String Authorization) {

        try {
            User currentUser = userService.getLoggedInUser(Authorization).orElse(null);
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            BrowsingHistory browsingHistory = browsingHistoryService.getBrowsingHistoryByUserId(currentUser);


            Optional<Book> book = bookService.findBookByName(name);
            if (!book.isPresent()) {
                return ResponseEntity.notFound().build();
            } else {
                String genre = book.get().getGenre();
                int count = browsingHistory.getCategoriesFrequency().getOrDefault(genre, 0);
                browsingHistory.getCategoriesFrequency().put(genre, count + 1);
                browsingHistoryService.save(browsingHistory);

                return ResponseEntity.ok(book.get());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> search(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "startDate", required = false) Date startDate,
            @RequestParam(value = "endDate", required = false) Date endDate) {

        try {
            List<Book> suggestions = bookService
                    .getAutocompleteSuggestions(query, author, category, minPrice, maxPrice, startDate, endDate);
            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/books/{name}/add-to-cart")
    public ResponseEntity<String> addToCart(@PathVariable String name, @RequestHeader String Authorization) {
        try {
            User currentUser = userService.getLoggedInUser(Authorization).get();

            ShoppingCart shoppingCart = shoppingCartService.GetByUserId(currentUser.getId());


            Optional<Book> book = bookService.findBookByName(name);
            if (book.isPresent()) {
                shoppingCart.getBooks().add(book.get());
                shoppingCartService.save(shoppingCart);
                return ResponseEntity.ok(shoppingCart.toString());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/books/{name}/add-to-wishlist")
    public ResponseEntity<?> addToWishlist(@PathVariable String name, @RequestHeader String Authorization) {
        try {
            User currentUser = userService.getLoggedInUser(Authorization).get();

            WishList wishList = wishListService.GetByUserId(currentUser.getId());


            Optional<Book> book = bookService.findBookByName(name);
            if (book.isPresent()) {
                wishList.getBooks().add(book.get());
                wishListService.save(wishList);
                return ResponseEntity.ok(wishList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @DeleteMapping("admin/books/{name}")
    public ResponseEntity<String> deleteBook(@PathVariable String name) {
        Optional<Book> book = bookService.findBookByName(name);
        if (book.isPresent()) {
            bookService.deleteBookByName(name);
            return ResponseEntity.ok("Book deleted");
        } else {
            return ResponseEntity.badRequest().body("Book not found: " + name);
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

    @PostMapping("/books/{bookName}/Review")
    public ResponseEntity<String> leaveReview(@PathVariable String bookName, @RequestBody Review review) {
        Optional<Book> book = bookService.findBookByName(bookName);
        if (book.isPresent()) {
            review.setBookId(book.get());
            Review savedReview = reviewService.save(review);
            return ResponseEntity.ok(savedReview.toString());
        } else {
            return ResponseEntity.badRequest().body("Book not found: " + bookName);
        }
    }


}