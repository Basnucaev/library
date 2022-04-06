package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.FileBookNotFoundException;
import com.books.basnucaev.library.exception.FileNotFoundInLocalPathException;
import com.books.basnucaev.library.exception.NotAcceptableFileFormatException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.FileBookRepository;
import com.books.basnucaev.library.service.BookFormats;
import com.books.basnucaev.library.service.FileBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FileBookServiceImplementation implements FileBookService {
    private final FileBookRepository fileBookRepository;
    private final String UPLOAD_FOLDER = "C:\\Users\\6\\Desktop\\BooksStorage\\";

    @Autowired
    public FileBookServiceImplementation(FileBookRepository fileBookRepository) {
        this.fileBookRepository = fileBookRepository;
    }

    @Override
    public FileBook getFileById(int id) {
        Optional<FileBook> optional = fileBookRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new FileBookNotFoundException(Response.NOT_FOUND_BY_ID.getId(id));
        }
    }

    @Override
    public void addDownloadUriToAllFilesBook(Book book) {
        List<FileBook> fileBooks = fileBookRepository.findAllByBookId(book.getId());
        for (FileBook fileBook : fileBooks) {
            if (fileBook.getDownloadURI() == null) {
                fileBook.setDownloadURI(getDownloadUri(fileBook));
                fileBookRepository.save(fileBook);
            }
        }
    }

    @Override
    public FileBook uploadFileToLocalFolder(MultipartFile file, Book book) {
        if (!fileTypeCheck(file.getContentType())) {
            throw new NotAcceptableFileFormatException("You can upload only these formats: " +
                    Arrays.toString(BookFormats.formatsAbbreviation));
        }
        String finalFilePath = UPLOAD_FOLDER + file.getOriginalFilename();
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalFilePath)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new FileBook(file.getOriginalFilename(), file.getContentType(),
                finalFilePath, file.getSize());
    }

    @Override
    public boolean addFileToBookById(Book book, MultipartFile file) {
        FileBook toAddFile = uploadFileToLocalFolder(file, book);
        book.addFileBook(toAddFile);
        return true;
    }

    @Override
    public byte[] downloadFromLocalFolder(FileBook fileBook) {
        File file = new File(fileBook.getFilePath());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            if (file.exists()) {
                return fileInputStream.readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new FileNotFoundInLocalPathException("File does not exist");
    }

    private boolean fileTypeCheck(String contentType) {
        for (String type : BookFormats.formats) {
            if (type.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private String getDownloadUri(FileBook fileBook) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().
                path("/api/books/download/").
                path(String.valueOf(fileBook.getId())).
                toUriString();
    }
}
