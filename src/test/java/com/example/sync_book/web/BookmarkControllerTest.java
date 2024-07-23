package com.example.sync_book.web;

import com.example.sync_book.error.NotFoundException;
import com.example.sync_book.service.BookmarkService;
import com.example.sync_book.to.BookmarkTo;
import com.example.sync_book.util.BookmarkTestData;
import com.example.sync_book.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.sync_book.util.BookTestData.*;
import static com.example.sync_book.util.BookmarkTestData.*;
import static com.example.sync_book.util.CommonTestData.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookmarkControllerTest extends AbstractControllerTest {

    private static final String REST_URL = BookmarkController.REST_URL;

    @Autowired
    private BookmarkService service;

    @Test
    void createHtmlUnsafe() throws Exception {
        BookmarkTo bookmarkUnsafe = getUnsafe();

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookmarkUnsafe)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        BookmarkTo newBookmark = BookmarkTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newBookmark)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        BookmarkTo created = BOOKMARK_TO_MATCHER.readFromJson(action);
        BOOKMARK_TO_MATCHER.assertMatch(created, newBookmark);
        Integer newId = created.getId();
        BOOKMARK_TO_MATCHER.assertMatch(service.getById(newId), newBookmark);
    }

    @Test
    void update() throws Exception {
        BookmarkTo updated = BookmarkTestData.getUpdated();

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + BOOKMARK1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        BOOKMARK_TO_MATCHER.assertMatch(service.getById(BOOKMARK1_ID), updated);
    }

    @Test
    void getById() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + BOOKMARK1_ID))
                .andExpect(status().isOk())
                .andExpect(BOOKMARK_TO_MATCHER.contentJson(BOOKMARK1));
    }

    @Test
    void getAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/user/" + USER1_ID))
                .andExpect(status().isOk())
                .andExpect(BOOKMARK_TO_MATCHER.contentJson(BOOKMARK_LIST));
    }

    @Test
    void deleteById() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + BOOKMARK1_ID))
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> service.getById(BOOKMARK1_ID));
    }

    @Test
    void deleteAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/user/" + USER1_ID))
                .andExpect(status().isNoContent());

        assertTrue(service.getAllByUserId(USER1_ID).isEmpty());
    }

    @Test
    void createInvalid() throws Exception {
        BookmarkTo bookmarkInvalid = getInvalid();

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookmarkInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        BookmarkTo bookInvalid = BookmarkTestData.getInvalid();

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + BOOKMARK1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        BookmarkTo bookmarkUnsafe = getUnsafe();

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + BOOK1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(bookmarkUnsafe)))
                .andExpect(status().isUnprocessableEntity());
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
}