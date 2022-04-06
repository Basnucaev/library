package com.books.basnucaev.library.controller;

import com.books.basnucaev.library.entity.Book;
import com.books.basnucaev.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {


    @Autowired
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void getBookByIdTest() throws Exception {
        Book book = new Book("Asd", "Asd", 100);

        when(bookService.getBookById(anyInt())).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/3")).
                andDo(print()).
                andExpect(jsonPath("$.title").value("Asd")).
                andExpect(jsonPath("$.author").value("Asd")).
                andExpect(jsonPath("$.price").value(100)).
                andExpect(status().isOk());
    }
}