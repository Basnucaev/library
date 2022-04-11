package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.FileBook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class Helper {

    public String getDownloadUri(FileBook fileBook) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/api/books/download/").
                path(String.valueOf(fileBook.getId())).
                toUriString();
    }
}
