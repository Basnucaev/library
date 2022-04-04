package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.service.FileBookService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileBookServiceImplementation implements FileBookService {
    @Override
    public FileBook uploadFileToLocalFolder(MultipartFile file, Book book, String path) {
        String finalFilePath = path + file.getOriginalFilename();
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalFilePath)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        FileBook fileBook = new FileBook();
        fileBook.setFileType(file.getContentType());
        fileBook.setFileName(file.getOriginalFilename());
        fileBook.setFilePath(finalFilePath);
        fileBook.setSize(file.getSize());
        return fileBook;
    }
}
