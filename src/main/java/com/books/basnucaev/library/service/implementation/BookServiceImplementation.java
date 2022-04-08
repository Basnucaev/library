package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.BookNotFoundException;
import com.books.basnucaev.library.exception.BookVarsEmptyException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.service.BookService;
import com.books.basnucaev.library.service.FileBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        FileBook fileBook = fileBookService.uploadFileToLocalFolder(file, book);
        book.addFileBook(fileBook);
        bookRepository.save(book);
        fileBookService.addDownloadUriToAllFilesBook(book);
        return true;
    }

    @Override
    public boolean updateBook(Book book) {
        Book toUpdateBook = getBookById(book.getId());
        if (book.getTitle() != null && !book.getTitle().equals(""))
            toUpdateBook.setTitle(book.getTitle());
        if (book.getAuthor() != null && !book.getAuthor().equals(""))
            toUpdateBook.setAuthor(book.getAuthor());
        if (book.getPrice() != 0)
            toUpdateBook.setPrice(book.getPrice());
        bookRepository.save(toUpdateBook);
        return true;
    }

    @Override
    public boolean deleteBookById(int id) {
        Book toDeleteBook = getBookById(id);
        bookRepository.delete(toDeleteBook);
        return true;
    }

    private boolean bookFieldsCheck(Book book) {
        return book.getTitle().equals("") || book.getAuthor().equals("") || book.getPrice() == 0;
    }

}
