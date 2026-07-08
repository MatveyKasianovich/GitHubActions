package com.example.demo;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
class BookService {

    public final Map<Long, Book> bookMap;
    private Long idCounter;

    public BookService() {
        this.bookMap = new HashMap<>();
        this.idCounter = 0L;
    }

    public List<Book> getAllBooks() {
        return bookMap.values().stream().toList();
    }

    public Book getBookById(Long id) {
        return Optional.ofNullable(bookMap.get(id))
                .orElseThrow(() -> new RuntimeException("Not found book by id=%s".formatted(id)));
    }

    public Book save(Book book) {
        Long newId = ++idCounter;
        var newBook = new Book(
                newId,
                book.title(),
                book.author(),
                book.isbn()
        );
        bookMap.put(newId, newBook);
        return newBook;
    }

    public Book update(Long id, Book bookToUpdate) {
        if (!bookMap.containsKey(id)) {
            throw new IllegalArgumentException("book with id=%s does not exist".formatted(id));
        }
        Book updatedBook = new Book(
                id,
                bookToUpdate.title(),
                bookToUpdate.author(),
                bookToUpdate.isbn()
        );
        bookMap.put(id, updatedBook);
        return updatedBook;
    }

    public void deleteBook(Long id) {
        if (!bookMap.containsKey(id)) {
            throw new IllegalArgumentException("book with id=%s does not exist".formatted(id));
        }
        bookMap.remove(id);
    }
}