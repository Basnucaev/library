package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.BookFile;
import com.books.basnucaev.library.service.BookFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
public class BookFileController {
    private final BookFileService bookFileService;

    @Autowired
    public BookFileController(BookFileService bookFileService) {
        this.bookFileService = bookFileService;
    }

    @PostMapping("/upload")
    public String uploadFileBook(@RequestParam(name = "file") MultipartFile file) {
        BookFile bookFile = bookFileService.getBookFileFromMultipartFile(file);
        bookFileService.addBookFileToDB(bookFile);
        return "File was upload successfully";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFileBook(@PathVariable int id) {
        BookFile bookFile = bookFileService.downloadBookFile(id);

        return ResponseEntity.ok().
                contentType(MediaType.parseMediaType(bookFile.getFileType())).
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + bookFile.getFileName()).
                body(new ByteArrayResource(bookFile.getFileData()));
    }
}
