package com.books.basnucaev.library.exception;

public class FileNotFoundInLocalPathException extends RuntimeException {

    public FileNotFoundInLocalPathException() {
    }

    public FileNotFoundInLocalPathException(String message) {
        super(message);
    }

    public FileNotFoundInLocalPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundInLocalPathException(Throwable cause) {
        super(cause);
    }
}
