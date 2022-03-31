package com.books.basnucaev.library.service.implimentation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getOneBook(int id) {
        return bookRepository.getBookById(id);
    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Override
    public boolean updateBook(Book book) {
        for (Book bookTemp : getAllBooks()) {
            if (bookTemp.getId() == book.getId())
                bookRepository.save(book);
            return true;
        }
        return false;
    }
}