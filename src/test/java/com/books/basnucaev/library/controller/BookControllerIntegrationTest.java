package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Sql("/test.sql")
    void getBookByIdTest() {
        ResponseEntity<Book> response = testRestTemplate.getForEntity("/api/books/1", Book.class);

        assertEquals(1, response.getBody().getId());
        assertEquals("War and piece", response.getBody().getTitle());
        assertEquals("Tolstoy", response.getBody().getAuthor());
        assertEquals(699, response.getBody().getPrice());

    }

    @Test
    void addBookTest() {
        Book book = new Book("sd", "as", 100);

        HttpEntity<Book> request = new HttpEntity<>(book);
        ResponseEntity<Book> response = testRestTemplate.postForEntity("/api/books", request, Book.class);

        assertNotNull(response.getBody());
        assertNotEquals(0, request.getBody().getId());
        assertEquals("sd", response.getBody().getTitle());
        assertEquals("as", response.getBody().getAuthor());
        assertEquals(100, response.getBody().getPrice());
    }
}
