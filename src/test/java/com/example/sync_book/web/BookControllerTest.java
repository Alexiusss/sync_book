package com.example.sync_book.web;

import com.example.sync_book.error.NotFoundException;
import com.example.sync_book.model.Genre;
import com.example.sync_book.service.BookService;
import com.example.sync_book.to.BookTo;
import com.example.sync_book.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.example.sync_book.util.BookTestData.*;
import static com.example.sync_book.util.JsonUtil.writeValue;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest extends AbstractControllerTest {

    private static final String REST_URL = BookController.REST_URL;

    @Autowired
    BookService service;

    @Test
    void createHtmlUnsafe() throws Exception {
        BookTo bookUnsafe = getNew();
        bookUnsafe.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookUnsafe)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        BookTo newBook = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newBook)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        BookTo created = BOOK_TO_MATCHER.readFromJson(action);
        Integer newId = created.getId();
        newBook.setId(newId);
        BOOK_TO_MATCHER.assertMatch(created, newBook);
        BOOK_TO_MATCHER.assertMatch(service.get(newId), newBook);
    }

    @Test
    void createInvalid() throws Exception {
        BookTo bookInvalid = getNew();
        bookInvalid.setName("");

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        BookTo bookInvalid = getNew();
        bookInvalid.setId(BOOK1_ID);
        bookInvalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + BOOK1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        BookTo bookUnsafe = getNew();
        bookUnsafe.setId(BOOK1_ID);
        bookUnsafe.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + BOOK1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookUnsafe)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        BookTo updated = BOOK3;
        updated.setName("Updated name");

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + BOOK3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());

        BOOK_TO_MATCHER.assertMatch(service.get(BOOK3_ID), updated);
    }

    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + NOT_FOUND_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("detail", equalTo(NOT_FOUND_MESSAGE)));
    }

    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + NOT_FOUND_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("detail", equalTo(NOT_FOUND_MESSAGE)));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + BOOK2_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(BOOK2_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", equalTo(asParsedJson(BOOK_TO_LIST))));

    }

    @Test
    void gertAllByAuthorName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("author", AUTHOR_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", equalTo(asParsedJson(List.of(BOOK1)))));
    }

    @Test
    void gertAllByGenre() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("genre", Genre.FANTASY.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", equalTo(asParsedJson(List.of(BOOK3)))));
    }

    @Test
    void gertAllByPublicationYear() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("publicationYear", String.valueOf(PUBLICATION_YEAR)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", equalTo(asParsedJson(List.of(BOOK1)))));
    }

    @Test
    void gertAllByName() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .param("name", BOOK_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", equalTo(asParsedJson(List.of(BOOK1)))));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + BOOK1_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(BOOK_TO_MATCHER.contentJson(BOOK1));
    }
}