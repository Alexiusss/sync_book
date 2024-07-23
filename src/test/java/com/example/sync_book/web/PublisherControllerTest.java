package com.example.sync_book.web;

import com.example.sync_book.error.NotFoundException;
import com.example.sync_book.service.PublisherService;
import com.example.sync_book.to.PublisherTo;
import com.example.sync_book.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.sync_book.util.CommonTestData.NOT_FOUND_ID;
import static com.example.sync_book.util.CommonTestData.NOT_FOUND_MESSAGE;
import static com.example.sync_book.util.JsonUtil.writeValue;
import static com.example.sync_book.util.PublisherTestData.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PublisherControllerTest extends AbstractControllerTest {

    private static final String REST_URL = PublisherController.REST_URL;

    @Autowired
    private PublisherService service;

    @Test
    void createHtmlUnsafe() throws Exception {
        PublisherTo publisherUnsafe = getNew();
        publisherUnsafe.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(publisherUnsafe)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void create() throws Exception {
        PublisherTo newPublisher = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newPublisher)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        PublisherTo created = PUBLISHER_TO_MATCHER.readFromJson(action);
        Integer newId = created.getId();
        newPublisher.setId(newId);
        PUBLISHER_TO_MATCHER.assertMatch(created, newPublisher);
        PUBLISHER_TO_MATCHER.assertMatch(service.get(newId), newPublisher);

    }

    @Test
    void createInvalid() throws Exception {
        PublisherTo publisherInvalid = getNew();
        publisherInvalid.setName("");

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(publisherInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateInvalid() throws Exception {
        PublisherTo publisherInvalid = PUBLISHER1;
        publisherInvalid.setName("");

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + PUBLISHER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(publisherInvalid)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateHtmlUnsafe() throws Exception {
        PublisherTo publisherUnsafe = PUBLISHER1;
        publisherUnsafe.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + PUBLISHER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(publisherUnsafe)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void update() throws Exception {
        PublisherTo updated = PUBLISHER3;
        updated.setName("Updated name");

        perform(MockMvcRequestBuilders.put(REST_URL + "/" + PUBLISHER3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andExpect(status().isNoContent());

        PUBLISHER_TO_MATCHER.assertMatch(service.get(PUBLISHER3_ID), updated);
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
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + PUBLISHER2_ID))
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> service.get(PUBLISHER2_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(PUBLISHER_TO_MATCHER.contentJson(PUBLISHER_TO_LIST));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + PUBLISHER1_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(PUBLISHER_TO_MATCHER.contentJson(PUBLISHER1));
    }
}