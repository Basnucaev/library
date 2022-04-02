package com.books.basnucaev.library.exception.my_exceptions;

public class FileNotFoundInLocalPathException extends RuntimeException {

    public FileNotFoundInLocalPathException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
