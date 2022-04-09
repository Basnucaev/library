package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.BookNotFoundException;
import com.books.basnucaev.library.exception.BookVarsEmptyException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.service.implementation.BookServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private FileBookService fileBookService;

    @BeforeEach
    void setUp() {
        bookService = new BookServiceImplementation(bookRepository, fileBookService);
    }

    @Test
    void shouldThrowExceptionWhenTryToGetBookByNonExistingId() {
        // given
        int id = 1;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> bookService.getBookById(id)).
                isInstanceOf(BookNotFoundException.class).
                hasMessageContaining(Response.NOT_FOUND_BY_ID.getId(id));
    }

    @Test
    void shouldThrowExceptionWhenBooksVarsAreEmptyWhenAddingBook() {
        // given
        Book book = new Book("", "", 0);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        // when

        // then
        assertThatThrownBy(() -> bookService.addBook(book, file)).
                isInstanceOf(BookVarsEmptyException.class).
                hasMessageContaining("Book vars are empty");

        verify(bookRepository, never()).save(any());
        verify(fileBookService, never()).addDownloadUriToAllFilesBook(any());
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
        Book book = new Book("title", "author", 1500);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        // when
        Book expected = bookService.getBookById(anyInt());

        // then
        verify(bookRepository).findById(anyInt());
        assertThat(expected).isEqualTo(book);

    }

    @Test
    void canAddBook() {
        // given
        Book book = new Book("title", "author", 1500);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        when(fileBookService.uploadFileToLocalFolder(file, book)).thenReturn(new FileBook());

        // when
        bookService.addBook(book, file);

        // then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook).isEqualTo(book);
    }

    @Test
    void canUpdateBook() {
        // given
        Book book = new Book("title", "author", 1500);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        // when
        bookService.updateBook(book);

        // then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository).save(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook).isEqualTo(book);
    }

    @Test
    void canDeleteBookById() {
        // given
        Book book = new Book("title", "author", 1500);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        // when
        bookService.deleteBookById(anyInt());

        // then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository).delete(bookArgumentCaptor.capture());
        Book capturedBook = bookArgumentCaptor.getValue();
        assertThat(capturedBook).isEqualTo(book);
    }
}