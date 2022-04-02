package com.books.basnucaev.library.service;

import java.util.UUID;

public interface BookFileService {

    void uploadToLocalFolder(byte[] file, UUID uuid);

    // У меня в базе не хранится чистое имя файла, поэтому я не могу передать ему имя файла
    // только если через манипуляции со String ,можно было сделать отдельную тиблицу даже
    // не храня в ней файлы, хранить чисто харектеристики файла (имя, тип, размер и т.д.)
    // но мне самому очень впадлу заморачиваться, но если ты напишешь брат, я сделаю
    byte[] downloadFromLocalPath(String filePath);

    boolean deleteFileFromLocalFolder(String filePath);
}
