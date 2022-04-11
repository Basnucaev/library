package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibraryLogicController.class)
class LibraryLogicControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LibraryService libraryService;

    private final String booksJSON =
            "[" +
                    "{" +
                    "\"title\":\"title\"," +
                    "\"author\":\"author\"," +
                    "\"price\":1000.0" +
                    "}," +
                    "{" +
                    "\"title\":\"titl\"," +
                    "\"author\":\"autho\"" +
                    ",\"price\":2000.0" +
                    "}" +
                    "]";

    @Test
    void shouldGetAllBooksWithTitleContainsAndStatusIsOk() throws Exception {
        String title = "tit";

        List<Book> books = List.of(
                new Book("title", "author", 1000),
                new Book("titl", "autho", 2000));
        when(libraryService.getBooksByTittleContains(title)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/find/title").
                        param("title", title)).
                andExpect(content().json(booksJSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldGetAllBooksWithAuthorContainsAndStatusIsOk() throws Exception {
        List<Book> books = List.of(
                new Book("title", "author", 1000),
                new Book("titl", "autho", 2000));
        when(libraryService.getBooksByAuthorContains("auth")).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/find/author").
                        param("author", "auth")).
                andExpect(content().json(booksJSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldGetAllBooksByPriceDiapasonAndStatusIsOk() throws Exception {
        List<Book> books = List.of(
                new Book("title", "author", 1000),
                new Book("titl", "autho", 2000));
        when(libraryService.getBooksByPriceDiapason(500, 2500)).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/find/price").
                        param("from", "500").
                        param("to", "2500")).
                andExpect(content().json(booksJSON)).
                andExpect(status().isOk());
    }
}