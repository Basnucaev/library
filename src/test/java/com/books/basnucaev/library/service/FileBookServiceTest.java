package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.BookNotFoundException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.repository.FileBookRepository;
import com.books.basnucaev.library.service.implementation.FileBookServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileBookServiceTest {
    private FileBookService fileBookService;
    private final String FILES_STORAGE = "C:\\Users\\6\\Desktop\\BooksStorage\\Files\\";

    @Mock
    private BookRepository bookRepository;
    @Mock
    private FileBookRepository fileBookRepository;

    @BeforeEach
    void setUp() {
        fileBookService = new FileBookServiceImplementation(fileBookRepository, bookRepository, FILES_STORAGE);
    }

    @Test
    void shouldThrowExceptionWhenTryToGetFileBookByNonExistingId() {
        // given
        int id = 1;
        when(fileBookRepository.findById(id)).thenReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> fileBookService.getFileById(id)).
                isInstanceOf(BookNotFoundException.class).
                hasMessageContaining(Response.NOT_FOUND_BY_ID.getId(id));
    }

    @Test
    void shouldThrowExceptionWhenTryToDownloadNonExistedFile() {
        // given

        // when

        // then
    }

    @Test
    void shouldThrowExceptionWhenTryToUploadNotAllowedType() {
        // given

        // when

        // then
    }

    @Test
    void canUploadFileToLocalFolder() {
        // given
        Book book = new Book("title", "author", 1500);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        String uuid = String.valueOf(UUID.randomUUID());
        FileBook fileBook = new FileBook(uuid, file.getContentType(), FILES_STORAGE + uuid, file.getSize());

        // when
        FileBook expected = fileBookService.uploadFileToLocalFolder(file, book);
        expected.setFileName(uuid);
        expected.setFilePath(FILES_STORAGE + uuid);

        // then
        assertThat(expected.toString()).isEqualTo(fileBook.toString());
    }

    @Test
    void canAddFileToBookById() {
        // given
        Book book = new Book("title", "author", 1500);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        // when
        fileBookService.addFileToBookById(book, file);

        // then
        ArgumentCaptor<FileBook> fileBookArgumentCaptor = ArgumentCaptor.forClass(FileBook.class);
        verify(fileBookRepository).save(fileBookArgumentCaptor.capture());
        verify(bookRepository).save(book);

        FileBook fileBook = fileBookArgumentCaptor.getValue();
        assertThat(book.getFileBooks().contains(fileBook)).isTrue();
    }

    @Test
    void downloadFromLocalFolder() {
        // given
        File file = new File(FILES_STORAGE + "test_file.pdf");
        String uuid = String.valueOf(UUID.randomUUID());

        FileBook fileBook = new FileBook(uuid, MediaType.APPLICATION_PDF_VALUE, file.getPath(), 12000);

        // when
        byte[] bytesFromFileBook = fileBookService.downloadFromLocalFolder(fileBook);

        // then
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] expected = fileInputStream.readAllBytes();
            assertThat(expected).isEqualTo(bytesFromFileBook);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getFileById() {
        // given
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        String uuid = String.valueOf(UUID.randomUUID());
        FileBook fileBook = new FileBook(uuid, file.getContentType(), FILES_STORAGE + uuid, file.getSize());

        when(fileBookRepository.findById(anyInt())).thenReturn(Optional.of(fileBook));

        // when
        FileBook expected = fileBookService.getFileById(anyInt());

        // then
        verify(fileBookRepository).findById(anyInt());
        assertThat(expected).isEqualTo(fileBook);
    }

    @Test
    @Disabled
    void addDownloadUriToAllFilesBook() {
        // given
        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        Book book = new Book("title", "author", 1500);

        List<FileBook> fileBooks = new ArrayList<>();
        String uuid = String.valueOf(UUID.randomUUID());
        fileBooks.add(new FileBook(uuid, "pdf", FILES_STORAGE + uuid, 10000));
        String uuid2 = String.valueOf(UUID.randomUUID());
        fileBooks.add(new FileBook(uuid2, "docx", FILES_STORAGE + uuid, 5000));

        book.setFileBooks(fileBooks);

        when(fileBookRepository.findAllFilesByBookId(anyInt())).thenReturn(fileBooks);
        when(request.getContextPath()).thenReturn("http://localhost:8090/");

        // when
        fileBookService.addDownloadUriToAllFilesBook(book);

        // then
        verify(fileBookRepository).findAllFilesByBookId(anyInt());
        for (FileBook fileBook : fileBooks) {
            assertThat(fileBook.getDownloadURI()).isNotNull();
            verify(fileBookRepository).save(fileBook);
        }
    }
}