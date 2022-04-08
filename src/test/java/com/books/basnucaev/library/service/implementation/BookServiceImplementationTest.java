package com.books.basnucaev.library.service.implementation;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.repository.FileBookRepository;
import com.books.basnucaev.library.service.BookService;
import com.books.basnucaev.library.service.FileBookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceImplementationTest {
    @Value("${library.books.storage-folder-path}")
    private String files_storage;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private FileBookRepository fileBookRepository;

    private FileBookService fileBookService;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImplementation(bookRepository, fileBookService);
        fileBookService = new FileBookServiceImplementation(fileBookRepository, bookRepository, files_storage);
    }

    @Test
    void canGetAllBooks() {
        // when
        bookService.getAllBooks();

        // then
        verify(bookRepository).findAll();
    }

    @Test
    void canGetBookById() {
        // given

        // when

        // then
    }

    @Test
    @Disabled
    void canAddBook() {
        // given
        Book book = new Book("title", "author", 1500);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        // when
        bookService.addBook(book, file);

        // then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();

        assertThat(capturedBook).isEqualTo(book);
    }

    @Test
    void updateBook() {
        // given

        // when

        // then
    }

    @Test
    void deleteBookById() {
        // given
        // when
        bookService.deleteBookById(anyInt());
        // доработать позже, тут выбрасывается исключение, не могу написать этот тест не разобравшись с addBook

        // then
        verify(bookRepository).delete(any());
    }
}