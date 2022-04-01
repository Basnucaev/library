package com.books.basnucaev.library.service;

import org.springframework.web.multipart.MultipartFile;

public interface BookFileService {

    void uploadToLocalFolder(MultipartFile file);

    byte[] downloadFromLocalPath(String path);
}
