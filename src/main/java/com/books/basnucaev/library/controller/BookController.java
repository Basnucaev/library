package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.BookFileService;
import com.books.basnucaev.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final BookFileService fileService;

    @Autowired
    public BookController(BookService bookService, BookFileService fileService) {
        this.bookService = bookService;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Book>> getAll() {
        List<Book> books = bookService.getAllBooks();

        return books != null && !books.isEmpty() ?
                new ResponseEntity<>(books, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable(name = "id") int id) {
        Book book = bookService.getOneBook(id);

        return book != null ?
                new ResponseEntity<>(book, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        bookService.addBook(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        return bookService.updateBook(book) ?
                new ResponseEntity<>(book, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        Book book = bookService.getOneBook(id);
        if (book != null) {
            fileService.deleteFileFromLocalFolder(book.getFilePath());
            bookService.deleteBook(id);
            return new ResponseEntity<>("Book with id = " + id + " was deleted", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book with id = " + id + " not found", HttpStatus.NOT_MODIFIED);
        }
    }
}
