//package com.example.bookstore;
//
//import com.example.bookstore.domain.Book;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class BookApiTest extends  AbstractTest{
//    @Override
//    @Before
//    public void setUp() {
//        super.setUp();
//    }
//    @Test
//    public void getBooksList() throws Exception {
//        String uri = "/books/oliver twist";
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
//                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//
//        int status = mvcResult.getResponse().getStatus();
//        assertEquals(200, status);
//        String content = mvcResult.getResponse().getContentAsString();
//        Book Book = super.mapFromJson(content, Book.class);
//        assertTrue(Book.getName().equals("oliver twist"));
//    }
//}
