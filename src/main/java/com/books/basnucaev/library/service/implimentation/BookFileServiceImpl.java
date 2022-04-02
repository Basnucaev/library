package com.books.basnucaev.library.service.implimentation;

import com.books.basnucaev.library.exception.my_exceptions.FileNotFoundInLocalPathException;
import com.books.basnucaev.library.service.BookFileService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Service
public class BookFileServiceImpl implements BookFileService {
    private final String uploadFolderPath = "C:\\Users\\6\\Desktop\\BooksStorage\\";

    @Override
    public void uploadToLocalFolder(byte[] file, UUID uuid) {
        try {
            Path path = Paths.get(uploadFolderPath + uuid);
            Files.write(path, file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public byte[] downloadFromLocalPath(String filePath) {
        File file = new File(uploadFolderPath + filePath);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            if (file.exists()) {
                return fileInputStream.readAllBytes();
            } else {
                throw new FileNotFoundInLocalPathException("Local file not found");
            }
            // На счет перебрасывать IOException в IllegalStateException не понял :(
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean deleteFileFromLocalFolder(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }
}
