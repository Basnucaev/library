package com.books.basnucaev.library.repository;

import com.books.basnucaev.library.entity.FileBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileBookRepository extends JpaRepository<FileBook, Integer> {
}
