package com.books.basnucaev.library.exception;

public class NotAcceptableFileFormatException extends RuntimeException {
    public NotAcceptableFileFormatException() {
    }

    public NotAcceptableFileFormatException(String message) {
        super(message);
    }

    public NotAcceptableFileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAcceptableFileFormatException(Throwable cause) {
        super(cause);
    }
}
