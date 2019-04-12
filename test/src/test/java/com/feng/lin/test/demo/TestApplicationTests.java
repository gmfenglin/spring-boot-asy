package com.feng.lin.test.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.feng.lin.web.lib.config.property.ThreadPoolProperty;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {

	@Autowired
	private ThreadPoolProperty threadPoolProperty;
	@Test
	public void contextLoads() {
		System.out.println(threadPoolProperty.getCorePoolSize());
	}

}
