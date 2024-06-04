package com.example.sync_book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AbstractControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	protected ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
		return mockMvc.perform(builder);
	}
}