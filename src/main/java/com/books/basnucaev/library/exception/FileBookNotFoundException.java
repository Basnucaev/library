package com.books.basnucaev.library.exception;

public class FileBookNotFoundException extends RuntimeException {

    public FileBookNotFoundException() {
    }

    public FileBookNotFoundException(Response response) {
    }


    public FileBookNotFoundException(String message) {
        super(message);
    }

    public FileBookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileBookNotFoundException(Throwable cause) {
        super(cause);
    }
}
