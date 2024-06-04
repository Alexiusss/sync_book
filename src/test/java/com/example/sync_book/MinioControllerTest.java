package com.example.sync_book;

import com.example.sync_book.web.MinioController;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MinioControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MinioController.REST_URL;

    @Test
    void upload() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL))
                .andExpect(status().isCreated());
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk());
    }
}