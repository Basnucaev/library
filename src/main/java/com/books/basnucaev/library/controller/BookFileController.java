package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.BookFileService;
import com.books.basnucaev.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookFileController {
    private final BookFileService bookFileService;
    private final BookService bookService;

    @Autowired
    public BookFileController(BookFileService bookFileService, BookService bookService) {
        this.bookFileService = bookFileService;
        this.bookService = bookService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFileBook(@RequestParam(name = "file") MultipartFile file,
                                            @RequestParam("book_id") int bookId) throws IOException {
        Book book = bookService.getOneBook(bookId);
        String fileType = file.getContentType();

        if (!isFileTypePdf(fileType)) {
            return new ResponseEntity<>("You can only upload pdf files", HttpStatus.NOT_ACCEPTABLE);
        }
        if (book != null) {
            if (isBookAlreadyHaveFile(book)) {
                return new ResponseEntity<>("The book with id = " + book.getId() + " already has a file",
                        HttpStatus.NOT_ACCEPTABLE);
            }
            UUID uuid = UUID.randomUUID();
            book.setFilePath("C:\\Users\\6\\Desktop\\BooksStorage\\" + uuid);
            book.setFileType(fileType);
            book.setDownloadUri(getDownloadUri(book));
            bookService.updateBook(book);
            bookFileService.uploadToLocalFolder(file.getBytes(), uuid);
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
    }

    private boolean isBookAlreadyHaveFile(Book book) {
        return book.getFilePath() != null;
    }

    @GetMapping("/download/{title}")
    public ResponseEntity<?> downloadFileBook(@PathVariable String title) {
        Book book = bookService.getBookByTittle(title);
        if (book != null) {
            if (isBookFilePathExisted(book.getFilePath())) {
                byte[] file = bookFileService.downloadFromLocalPath(book.getFilePath());
                return ResponseEntity.ok().
                        contentType(MediaType.parseMediaType(book.getFileType())).
                        header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + book.getTitle()).
                        body(new ByteArrayResource(file));
            } else {
                return new ResponseEntity<>("This book has no file behind him, wait till user add a file to book",
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>("Cant find book by tittle: " + title, HttpStatus.NOT_FOUND);
        }
    }

    private boolean isBookFilePathExisted(String filePath) {
        return filePath != null;
    }

    private boolean isFileTypePdf(String contentType) {
        if (contentType == null) {
            return false;
        }
        return contentType.contains("pdf");
    }

    private String getDownloadUri(Book book) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/api/books/download/").
                path(book.getTitle()).
                toUriString();
    }
}
