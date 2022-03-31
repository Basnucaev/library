package com.books.basnucaev.library.service.implimentation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.BookFile;
import com.books.basnucaev.library.repository.BookFileRepository;
import com.books.basnucaev.library.service.BookFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BookFileServiceImpl implements BookFileService {
    private final BookFileRepository bookFileRepository;

    @Autowired
    public BookFileServiceImpl(BookFileRepository bookFileRepository) {
        this.bookFileRepository = bookFileRepository;
    }

    @Override
    public BookFile getBookFileFromMultipartFile(MultipartFile file) {
        BookFile bookFile = new BookFile();
        try {
            bookFile.setFileData(file.getBytes());
            bookFile.setFileType(file.getContentType());
            bookFile.setFileName(file.getOriginalFilename());
            bookFile.setSize(file.getSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookFile;
    }

    @Override
    public void addBookFileToDB(BookFile bookFile) {
        bookFileRepository.save(bookFile);
    }

    @Override
    public BookFile downloadBookFile(int id) {
        return bookFileRepository.findById(id).orElse(null);
    }
}
