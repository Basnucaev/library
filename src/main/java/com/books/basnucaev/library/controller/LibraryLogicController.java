package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class LibraryLogicController {
    private final LibraryService libraryService;

    @Autowired
    public LibraryLogicController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/books/find/title")
    public ResponseEntity<List<Book>> getBooksByTitleContains(@RequestParam("title") String title) {
        List<Book> foundBooks = libraryService.getBooksByTittleContains(title);
        return response(foundBooks);
    }

    @GetMapping("/books/find/author")
    public ResponseEntity<List<Book>> getBooksByAuthorContains(@RequestParam("author") String author) {
        List<Book> foundBooks = libraryService.getBooksByAuthorContains(author);
        return response(foundBooks);
    }

    @GetMapping("/books/find/price")
    public ResponseEntity<List<Book>> getBooksByPriceDiapason(@RequestParam("from") int from,
                                                              @RequestParam("to") int to) {
        List<Book> foundBooks = libraryService.getBooksByPriceDiapason(from, to);
        return response(foundBooks);
    }

    private ResponseEntity<List<Book>> response(List<Book> foundBooks) {
        if (foundBooks.isEmpty()) {
            return new ResponseEntity<>(foundBooks, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(foundBooks, HttpStatus.OK);
        }
    }
}
