package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.BookNotFoundException;
import com.books.basnucaev.library.exception.BookVarsEmptyException;
import com.books.basnucaev.library.exception.NotAcceptableFileFormatException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.service.BookFormats;
import com.books.basnucaev.library.service.BookService;
import com.books.basnucaev.library.service.FileBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImplementation implements BookService {
    private final BookRepository bookRepository;
    private final FileBookService fileBookService;
    private final String UPLOAD_FOLDER = "C:\\Users\\6\\Desktop\\BooksStorage\\";

    @Autowired
    public BookServiceImplementation(BookRepository bookRepository, FileBookService fileBookService) {
        this.bookRepository = bookRepository;
        this.fileBookService = fileBookService;
    }

    //--
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(int id) {
        Optional<Book> optional = bookRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new BookNotFoundException(Response.NOT_FOUND_BY_ID.getId(id));
        }
    }

    @Override
    public boolean addBook(Book book, MultipartFile file) {
        if (bookFieldsCheck(book)) {
            throw new BookVarsEmptyException("Book vars are empty");
        }
        if (!fileTypeCheck(file.getContentType())) {
            throw new NotAcceptableFileFormatException("You can upload only these formats: " +
                    Arrays.toString(BookFormats.formatsAbbreviation));
        }
        FileBook fileBook = fileBookService.uploadFileToLocalFolder(file, book, UPLOAD_FOLDER);
        book.addFileBook(fileBook);
        bookRepository.save(book);
        return true;
    }

    @Override
    public boolean deleteBookById(int id) {
        return false;
    }

    @Override
    public boolean updateBook(Book book) {
        return false;
    }

    private boolean bookFieldsCheck(Book book) {
        return book.getTitle().equals("") || book.getAuthor().equals("") || book.getPrice() == 0;
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
