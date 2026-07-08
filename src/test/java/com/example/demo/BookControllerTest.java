package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createBookSuccess() throws Exception {
        var book = new Book(
                null,
                "It",
                "Me",
                "123"
        );

        String bookJson = objectMapper.writeValueAsString(book);

        String createdBookJson = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
                )
                .andExpect(status().isNoContent())//неправильный статус для проверки
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book bookResponse = objectMapper.readValue(createdBookJson, Book.class);

        Assertions.assertNotNull(bookResponse.id());
        Assertions.assertEquals(book.title(), bookResponse.title());
        Assertions.assertEquals(book.author(), bookResponse.author());
        Assertions.assertEquals(book.isbn(), bookResponse.isbn());
    }

    @Test
    void getbookByIdSuccess() throws Exception {
        var book = new Book(
                null,
                "It",
                "Me",
                "123"
        );

        book = bookService.save(book);

        String foundBook = mockMvc.perform(get("/api/books/{id}", book.id()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book bookObject = objectMapper.readValue(foundBook, Book.class);

        Assertions.assertEquals(book.title(), bookObject.title());
        Assertions.assertEquals(book.author(), bookObject.author());
        Assertions.assertEquals(book.isbn(), bookObject.isbn());
        Assertions.assertEquals(book.id(), bookObject.id());
    }
    @Test
    void deleteBookByIdSuccess() throws Exception {

        var book = new Book(null, "It", "Me", "123");
        book = bookService.save(book);


        mockMvc.perform(delete("/api/books/{id}", book.id()))
                .andExpect(status().isOk());


    }
}
