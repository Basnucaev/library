package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryServiceImplementation implements LibraryService {
    private final BookRepository bookRepository;

    @Autowired
    public LibraryServiceImplementation(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getBooksByTittleContains(String tittle) {
        return bookRepository.findAllByTitleContains(tittle);
    }

    @Override
    public List<Book> getBooksByAuthorContains(String author) {
        return bookRepository.findAllByAuthorContains(author);
    }

    @Override
    public List<Book> getBooksByPriceDiapason(int from, int to) {
        return bookRepository.findAllByPriceBetween(from, to);
    }
}
