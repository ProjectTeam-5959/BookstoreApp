package org.example.bookstoreapp.domain.book.exception;

public class InvalidBookException extends RuntimeException  {
    public InvalidBookException(String message) {
        super(message);
    }
}
