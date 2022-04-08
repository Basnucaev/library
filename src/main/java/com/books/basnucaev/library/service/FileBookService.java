package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import org.springframework.web.multipart.MultipartFile;

public interface FileBookService {
    FileBook uploadFileToLocalFolder(MultipartFile file, Book book);

    void addFileToBookById(Book book, MultipartFile file);

    byte[] downloadFromLocalFolder(FileBook fileBook);

    FileBook getFileById(int id);

    void addDownloadUriToAllFilesBook(Book book);
}
