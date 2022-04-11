package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.FileBookNotFoundException;
import com.books.basnucaev.library.exception.FileNotFoundInLocalPathException;
import com.books.basnucaev.library.exception.NotAcceptableFileFormatException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.repository.FileBookRepository;
import com.books.basnucaev.library.service.BookFormats;
import com.books.basnucaev.library.service.FileBookService;
import com.books.basnucaev.library.service.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileBookServiceImplementation implements FileBookService {
    private final FileBookRepository fileBookRepository;
    private final BookRepository bookRepository;
    private final String FILES_STORAGE;
    private final Helper helper;

    @Autowired
    public FileBookServiceImplementation(FileBookRepository fileBookRepository,
                                         BookRepository bookRepository,
                                         @Value("${library.books.storage-folder-path}") String files_storage,
                                         Helper helper) {
        this.fileBookRepository = fileBookRepository;
        this.bookRepository = bookRepository;
        FILES_STORAGE = files_storage;
        this.helper = helper;
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
        List<FileBook> fileBooks = fileBookRepository.findAllFilesByBookId(book.getId());
        for (FileBook fileBook : fileBooks) {
            if (fileBook.getDownloadURI() == null) {
                fileBook.setDownloadURI(helper.getDownloadUri(fileBook));
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
        String uuid = String.valueOf(UUID.randomUUID());
        String finalFilePath = FILES_STORAGE + uuid;
        try (FileOutputStream fileOutputStream = new FileOutputStream(finalFilePath)) {
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new FileBook(uuid,
                file.getContentType(),
                finalFilePath,
                file.getSize());
    }

    @Override
    public void addFileToBookById(Book book, MultipartFile file) {
        FileBook toAddFile = uploadFileToLocalFolder(file, book);
        fileBookRepository.save(toAddFile);
        book.addFileBook(toAddFile);
        bookRepository.save(book);
    }

    @Override
    public byte[] downloadFromLocalFolder(FileBook fileBook) {
        if (fileBook.getFilePath() != null) {
            File file = new File(fileBook.getFilePath());
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                if (file.exists()) {
                    return fileInputStream.readAllBytes();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
