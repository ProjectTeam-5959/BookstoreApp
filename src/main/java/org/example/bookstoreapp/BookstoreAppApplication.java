package org.example.bookstoreapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class BookstoreAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookstoreAppApplication.class, args);
    }
}
