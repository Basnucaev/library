package com.books.basnucaev.library.service;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.exception.FileBookNotFoundException;
import com.books.basnucaev.library.exception.FileNotFoundInLocalPathException;
import com.books.basnucaev.library.exception.NotAcceptableFileFormatException;
import com.books.basnucaev.library.exception.Response;
import com.books.basnucaev.library.repository.BookRepository;
import com.books.basnucaev.library.repository.FileBookRepository;
import com.books.basnucaev.library.service.implementation.FileBookServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
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
    @Mock
    private Helper helper;

    @BeforeEach
    void setUp() {
        fileBookService = new FileBookServiceImplementation(fileBookRepository, bookRepository, FILES_STORAGE, helper);
    }

    @Test
    void shouldThrowExceptionWhenTryToGetFileBookByNonExistingId() {
        // given
        int id = 1;
        when(fileBookRepository.findById(id)).thenReturn(Optional.empty());

        // when

        // then
        assertThatThrownBy(() -> fileBookService.getFileById(id)).
                isInstanceOf(FileBookNotFoundException.class).
                hasMessageContaining(Response.NOT_FOUND_BY_ID.getId(id));
    }

    @Test
    void shouldThrowExceptionWhenTryToDownloadNonExistedFile() {
        // given

        // when

        // then
        assertThatThrownBy(() -> fileBookService.downloadFromLocalFolder(new FileBook())).
                isInstanceOf(FileNotFoundInLocalPathException.class).
                hasMessageContaining("File does not exist");
    }

    @Test
    void shouldThrowExceptionWhenTryToUploadNotAllowedType() {
        // given
        Book book = new Book("title", "author", 1500);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.IMAGE_GIF_VALUE, "Hello world".getBytes());

        // when

        // then
        assertThatThrownBy(() -> fileBookService.uploadFileToLocalFolder(file, book)).
                isInstanceOf(NotAcceptableFileFormatException.class).
                hasMessageContaining("You can upload only these formats: " +
                        Arrays.toString(BookFormats.formatsAbbreviation));
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
    void addDownloadUriToAllFilesBook() {
        // given
        String uuid = String.valueOf(UUID.randomUUID());
        String uuid2 = String.valueOf(UUID.randomUUID());
        Book book = new Book("title", "author", 1500);

        List<FileBook> fileBooks = List.of(new FileBook(uuid, "pdf", FILES_STORAGE + uuid, 10000),
                new FileBook(uuid2, "docx", FILES_STORAGE + uuid, 5000));

        book.setFileBooks(fileBooks);

        when(fileBookRepository.findAllFilesByBookId(anyInt())).thenReturn(fileBooks);
        when(helper.getDownloadUri(any(FileBook.class))).thenReturn("/api/books/download/");

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