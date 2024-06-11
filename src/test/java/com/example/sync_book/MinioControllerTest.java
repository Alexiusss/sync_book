package com.example.sync_book;

import com.example.sync_book.web.MinioController;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.example.sync_book.util.MinioTestData.NOT_FOUND_MESSAGE;
import static com.example.sync_book.util.MinioTestData.getNewFile;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class MinioControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MinioController.REST_URL;

    @Test
    void upload() throws Exception {
        MockMultipartFile file = getNewFile("test.txt", String.valueOf(MediaType.MULTIPART_FORM_DATA));
        perform(MockMvcRequestBuilders.multipart(REST_URL)
                .file(file))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().bytes(file.getOriginalFilename().getBytes()));
    }

    @Test
    void download() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/test.txt"))
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
}