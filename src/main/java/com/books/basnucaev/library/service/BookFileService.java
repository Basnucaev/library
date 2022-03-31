package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.BookFile;
import org.springframework.web.multipart.MultipartFile;

public interface BookFileService {

    BookFile getBookFileFromMultipartFile(MultipartFile file);

    void addBookFileToDB(BookFile bookFile);

    BookFile downloadBookFile(int id);
}
