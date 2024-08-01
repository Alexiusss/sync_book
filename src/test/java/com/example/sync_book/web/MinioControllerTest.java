package com.example.sync_book.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.sync_book.util.MinioTestData.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MinioControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MinioController.REST_URL;

    @Test
    void upload() throws Exception {
        MockMultipartFile file = getNewFile(FILE_NAME, String.valueOf(MediaType.MULTIPART_FORM_DATA));
        perform(MockMvcRequestBuilders.multipart(REST_URL)
                .file(file))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().bytes(file.getOriginalFilename().getBytes()));
    }

    @Test
    void download() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + FILE_NAME))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void downloadNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/not_found.txt"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("detail", equalTo(NOT_FOUND_MESSAGE)));

    }

    @Test
    void getFileSize() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + FILE_NAME + "/size"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("0"));
    }
}