package com.books.basnucaev.library.service.implimentation;

import com.books.basnucaev.library.service.BookFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class BookFileServiceImpl implements BookFileService {
    private final String uploadFolderPath = "C:\\Users\\6\\Desktop\\BooksStorage";

    @Override
    public void uploadToLocalFolder(MultipartFile file) {
        try {
            byte[] data = file.getBytes();
            Path path = Paths.get(uploadFolderPath + "\\" + file.getOriginalFilename());
            Files.write(path, data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFromLocalPath(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            return fileInputStream.readAllBytes();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
