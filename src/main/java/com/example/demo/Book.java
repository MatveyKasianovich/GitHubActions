package com.example.demo;

public record Book(
        Long id,
        String title,
        String author,
        String isbn
) {
}
