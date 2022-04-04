package com.books.basnucaev.library.exception;

public enum Response {
    NOT_FOUND_BY_ID {
        public String getId(long id) {
            return "Book by id= " + id + " not found";
        }
    };

    public abstract String getId(long id);
}
