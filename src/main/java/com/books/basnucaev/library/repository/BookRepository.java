package com.books.basnucaev.library.repository;

import com.books.basnucaev.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select b from Book b where b.title like %:title%")
    List<Book> findAllByTitleContains(String title);

    @Query("select b from Book b where b.author like %:author%")
    List<Book> findAllByAuthorContains(String author);

    List<Book> findAllByPriceBetween(double from, double to);
}
