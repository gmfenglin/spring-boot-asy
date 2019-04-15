package com.feng.lin.test.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TestApplicationTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void whenGetByIdSuccess() throws Exception {
		String result = mockMvc.perform(get("/test/1")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.id").value(1))
				.andReturn().getResponse().getContentAsString();
		
		System.out.println(result);
	}
	@Test
	public void whenGetByIdNoExists() throws Exception {
		String result = mockMvc.perform(get("/test/5")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(102))
				.andReturn().getResponse().getContentAsString();
		
		System.out.println(result);
	}
	@Test
	public void whenGetByIdLt1() throws Exception {
		String result = mockMvc.perform(get("/test/0")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
				.andExpect(jsonPath("$.data[0].defaultMessage").value("最小不能小于1"))
				.andReturn().getResponse().getContentAsString();
		
		System.out.println(result);
	}
	@Test
	public void whenGetByIdMissType() throws Exception {
		String result = mockMvc.perform(get("/test/a")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()))
				.andExpect(jsonPath("$.message").value("MethodArgumentTypeMismatchException"))
				.andReturn().getResponse().getContentAsString();
		
		System.out.println(result);
	}
	@Test
	public void whengetByCondition() throws Exception {
		String result = mockMvc.perform(get("/test")
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value(200))
				.andReturn().getResponse().getContentAsString();
		
		System.out.println(result);
	}

}
