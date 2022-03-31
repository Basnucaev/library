package com.books.basnucaev.library.repository;

import com.books.basnucaev.library.entity.BookFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {
}
