package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;

import java.util.List;

public interface LibraryService {

    List<Book> getBooksByTittleContains(String title);

    List<Book> getBooksByAuthorContains(String author);

    List<Book> getBooksByPriceDiapason(int from, int to);
}
