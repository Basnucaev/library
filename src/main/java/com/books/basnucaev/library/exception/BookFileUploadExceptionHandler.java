package com.books.basnucaev.library.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class BookFileUploadExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileUploadException(MaxUploadSizeExceededException exception) {
        return "You are trying to upload a file that is too large, the maximum file size is 150mb";
    }
}
