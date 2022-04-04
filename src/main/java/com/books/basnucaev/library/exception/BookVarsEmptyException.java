package com.books.basnucaev.library.exception;

public class BookVarsEmptyException extends RuntimeException {

    public BookVarsEmptyException() {
    }

    public BookVarsEmptyException(String message) {
        super(message);
    }

    public BookVarsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookVarsEmptyException(Throwable cause) {
        super(cause);
    }
}
