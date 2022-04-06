package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //--
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAll() {
        List<Book> books = bookService.getAllBooks();
        return response(books);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(name = "id") int id) {
        Book book = bookService.getBookById(id);
        return response(book);
    }

    @PostMapping("/books")
    public ResponseEntity<?> addBook(@RequestPart("book") Book book,
                                     @RequestPart("file") MultipartFile file) {
        return response(bookService.addBook(book, file), book, HttpStatus.CREATED);
    }

    @PutMapping("/books")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        return response(bookService.updateBook(book), book, HttpStatus.OK);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        return response(bookService.deleteBookById(id), "Book with id= " + id + " was deleted");
    }

    private ResponseEntity<List<Book>> response(List<Book> books) {
        if (books.isEmpty()) {
            return new ResponseEntity<>(books, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
    }

    private ResponseEntity<Book> response(Book book) {
        if (book == null) {
            return new ResponseEntity<>(book, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }
    }

    private ResponseEntity<Book> response(boolean value, Book book, HttpStatus httpStatus) {
        if (value) {
            return new ResponseEntity<>(book, httpStatus);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    private ResponseEntity<?> response(boolean value, Object object) {
        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
}
