package com.books.basnucaev.library.repository;

import com.books.basnucaev.library.entity.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    void itShouldFindAllBooksByTitleContains() {
        // given
        Book[] array = {
                new Book("abcdef", "author", 1500),
                new Book("abcde", "author", 1500),
                new Book("abcd", "author", 1500),
                new Book("abc", "author", 1500),
                new Book("ab", "author", 1500),
                new Book("a", "author", 1500)
        };
        List<Book> books = new ArrayList<>(Arrays.asList(array));
        bookRepository.saveAll(books);

        // when
        List<Book> foundedBooks = bookRepository.findAllBooksByTitleContains("a");

        // then
        assertThat(foundedBooks).isEqualTo(books);
    }

    @Test
    void itShouldFindAllBooksByAuthorContains() {
        // given
        Book[] array = {
                new Book("title", "abcdef", 1500),
                new Book("title", "abcde", 1500),
                new Book("title", "abcd", 1500),
                new Book("title", "abc", 1500),
                new Book("title", "ab", 1500),
                new Book("title", "a", 1500)
        };
        List<Book> books = new ArrayList<>(Arrays.asList(array));
        bookRepository.saveAll(books);

        // when
        List<Book> foundedBooks = bookRepository.findAllBooksByAuthorContains("a");

        // then
        assertThat(foundedBooks).isEqualTo(books);

    }
}