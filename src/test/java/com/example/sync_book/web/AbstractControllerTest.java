package com.example.sync_book.web;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.example.sync_book.util.MinioTestData.initMinioContainer;
import static com.example.sync_book.util.MinioTestData.setMinioTestProperties;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Sql(value = {"/test-data.sql"})
@Transactional
class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Container
    private static MinIOContainer minIOContainer = initMinioContainer();

    @BeforeAll
    static void setUp() {
        setMinioTestProperties(minIOContainer);
    }

    protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }
}