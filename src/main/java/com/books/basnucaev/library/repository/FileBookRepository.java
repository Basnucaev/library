package com.books.basnucaev.library.repository;

import com.books.basnucaev.library.entity.FileBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileBookRepository extends JpaRepository<FileBook, Integer> {

    List<FileBook> findAllFilesByBookId(int id);
}
