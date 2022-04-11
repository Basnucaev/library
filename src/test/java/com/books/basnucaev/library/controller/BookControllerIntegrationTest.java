package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private final String bookJSON =
            "{" +
                    "\"id\" : 1," +
                    "\"title\" : \"title\"," +
                    "\"author\" : \"author\"," +
                    "\"price\" : 1000" +
                    "}";

    private final String createBookJSON =
            "{" +
                    "\"title\" : \"title\"," +
                    "\"author\" : \"author\"," +
                    "\"price\" : 1000" +
                    "}";

    private final String booksJSON =
            "[" +
                    "{" +
                    "\"title\":\"title\"," +
                    "\"author\":\"author\"," +
                    "\"price\":1000.0" +
                    "}," +
                    "{" +
                    "\"title\":\"title\"," +
                    "\"author\":\"author\"" +
                    ",\"price\":2000.0" +
                    "}" +
                    "]";

    @Test
    void getAllBooksShouldReturnStatusOK() throws Exception {
        List<Book> books = List.of(new Book("title", "author", 1000),
                new Book("title", "author", 2000));
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")).
                andExpect(content().json(booksJSON)).
                andExpect(status().isOk());
    }

    @Test
    void getBookByIdShouldReturnStatusOk() throws Exception {
        Book book = new Book(1, "title", "author", 1000);
        when(bookService.getBookById(1)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/1")).
                andExpect(content().json(bookJSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldSaveBookAndReturnStatusCreated() throws Exception {
        // given
        MockMultipartFile bookMulti = new MockMultipartFile("book", null,
                MediaType.APPLICATION_JSON_VALUE, createBookJSON.getBytes());

        MockMultipartFile file = new MockMultipartFile("file", "hello.txt",
                MediaType.APPLICATION_PDF_VALUE, "Hello world".getBytes());

        // then
        mockMvc.perform(multipart("/api/books").
                        file(bookMulti).
                        file(file)).
                andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateBookAndReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books").
                        contentType(MediaType.APPLICATION_JSON).
                        content(bookJSON)).
                andExpect(status().isOk());
    }

    @Test
    void deleteBook() throws Exception {
        int id = 1;
        when(bookService.deleteBookById(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/" + id + "")).
                andExpect(content().string("Book with id= " + id + " was deleted")).
                andExpect(status().isOk());

    }
}
