package com.example.sync_book.web;

import com.example.sync_book.to.PublisherTo;
import com.example.sync_book.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.sync_book.util.PublisherTestData.getNew;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PublisherControllerTest extends AbstractControllerTest {

    private static final String REST_URL = PublisherController.REST_URL;

    @Test
    void createHtmlUnsafe() throws Exception {
        PublisherTo publisherUnsafe = getNew();
        publisherUnsafe.setName("<script>alert(123)</script>");

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(publisherUnsafe)))
                .andExpect(status().isUnprocessableEntity());
    }
}