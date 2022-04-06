package com.books.basnucaev.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> handleBookNotFoundException(BookNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>("Not valid request", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(BookVarsEmptyException.class)
    public ResponseEntity<?> handleBookVarsEmptyException(BookVarsEmptyException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NotAcceptableFileFormatException.class)
    public ResponseEntity<?> handleNotAcceptableFileFormatException(NotAcceptableFileFormatException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServletRequestPartException(MissingServletRequestPartException exception) {
        return new ResponseEntity<>(
                "Required request part \"" + exception.getRequestPartName() + "\" is not present",
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(FileBookNotFoundException.class)
    public ResponseEntity<?> handleFileBookNotFoundException(FileBookNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FileNotFoundInLocalPathException.class)
    public ResponseEntity<?> handleFileNotFoundInLocalPathException(FileNotFoundInLocalPathException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        return new ResponseEntity<>("Required request parameter \"" + exception.getName() + "\" is not present",
                HttpStatus.NOT_FOUND);
    }
}
