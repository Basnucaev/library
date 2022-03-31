package com.books.basnucaev.library.repository;

import com.books.basnucaev.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Book getBookById(int id);
}
