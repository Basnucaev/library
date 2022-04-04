package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(int id);

    boolean addBook(Book book, MultipartFile file);

    boolean deleteBookById(int id);

    boolean updateBook(Book book);
}
