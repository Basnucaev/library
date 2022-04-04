package com.books.basnucaev.library.service;

public class BookFormats {
    private static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final String FB2 = "application/octet-stream";
    private static final String EPUB = "application/epub+zip";
    private static final String PDF = "application/pdf";
    private static final String DOC = "application/msword";
    private static final String MOBI = "application/x-mobipocket-ebook";
    private static final String RTF = "application/rtf";
    private static final String DJVU = "image/vnd.djvu";
    private static final String TXT = "text/plain";

    public static String[] formats = {
            DOCX, FB2, EPUB, PDF, DOC, MOBI, RTF, DJVU, TXT
    };

    public static String[] formatsAbbreviation = {
            "DOCX", "FB2", "EPUB", "PDF", "DOC", "MOBI", "RTF", "DJVU", "TXT"
    };
}
