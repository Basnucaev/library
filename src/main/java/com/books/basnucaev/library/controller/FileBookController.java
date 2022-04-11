package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.service.BookService;
import com.books.basnucaev.library.service.FileBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/")
public class FileBookController {
    private final FileBookService fileBookService;
    private final BookService bookService;

    @Autowired
    public FileBookController(FileBookService fileBookService, BookService bookService) {
        this.fileBookService = fileBookService;
        this.bookService = bookService;
    }

    //--
    @PostMapping("books/{id}/upload-file")
    public ResponseEntity<Book> uploadFileToBookByBookId(@PathVariable int id, @RequestPart("file") MultipartFile file) {
        Book book = bookService.getBookById(id);
        fileBookService.addFileToBookById(book, file);
        fileBookService.addDownloadUriToAllFilesBook(book);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/books/download/{id}")
    public ResponseEntity<?> downloadByFileId(@PathVariable int id) {
        FileBook fileBook = fileBookService.getFileById(id);
        byte[] file = fileBookService.downloadFromLocalFolder(fileBook);

        return ResponseEntity.ok().
                contentType(MediaType.parseMediaType(fileBook.getFileType())).
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment; book name= " + fileBook.getFileName()).
                body(new ByteArrayResource(file));
    }

}
