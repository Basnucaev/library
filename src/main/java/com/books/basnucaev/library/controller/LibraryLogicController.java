package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class LibraryLogicController {
    private final BookService bookService;

    @Autowired
    public LibraryLogicController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/find/title")
    public List<Book> getBooksByTitleContains(@RequestParam("title") String title) {
        return bookService.getBooksByTittleContains(title);
    }

    @GetMapping("/find/author")
    public List<Book> getBooksByAuthorContains(@RequestParam("author") String author) {
        return bookService.getBooksByAuthorContains(author);
    }

    @GetMapping("/find/price")
    public List<Book> getBooksByPriceDiapason(@RequestParam("from") int from, @RequestParam("to") int to) {
        return bookService.getBooksByPriceDiapason(from, to);
    }
}
