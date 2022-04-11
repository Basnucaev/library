package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.entity.FileBook;
import com.books.basnucaev.library.service.BookService;
import com.books.basnucaev.library.service.FileBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileBookController.class)
class FileBookControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FileBookService fileBookService;

    @MockBean
    private BookService bookService;

    private final String bookJSON =
            "{" +
                    "\"id\" : 1," +
                    "\"title\" : \"title\"," +
                    "\"author\" : \"author\"," +
                    "\"price\" : 1000" +
                    "}";

    @Test
    void shouldUploadFileToBookAndReturnUpdatedBookWithStatusOk() throws Exception {
        int id = 1;
        Book book = new Book(id, "title", "author", 1000);
        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        when(bookService.getBookById(id)).thenReturn(book);

        mockMvc.perform(multipart("/api/books/" + id + "/upload-file").
                        file(file)).
                andExpect(status().isOk()).
                andExpect(content().json(bookJSON));
    }

    @Test
    void shouldReturnContentTypeWithFileBytesAndStatusIsOk() throws Exception {
        int id = 1;
        String uuid = String.valueOf(UUID.randomUUID());

        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        FileBook fileBook = new FileBook(uuid, file.getContentType(), uuid, file.getSize());

        when(fileBookService.getFileById(id)).thenReturn(fileBook);
        when(fileBookService.downloadFromLocalFolder(fileBook)).thenReturn(file.getBytes());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/download/" + id + "")).
                andExpect(content().contentType(MediaType.APPLICATION_PDF)).
                andExpect(content().bytes(file.getBytes())).
                andExpect(status().isOk());
    }
}