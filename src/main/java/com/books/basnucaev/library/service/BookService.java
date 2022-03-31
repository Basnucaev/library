package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllBooks();

    Book getOneBook(int id);

    void addBook(Book book);

    void deleteBook(int id);

    boolean updateBook(Book book);
}
