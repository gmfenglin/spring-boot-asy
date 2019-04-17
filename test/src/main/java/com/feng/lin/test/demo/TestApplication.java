package com.feng.lin.test.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.feng.lin.web.lib.controller.annotation.EnableFenglin;

@SpringBootApplication
@MapperScan(value="com.feng.lin.test.demo.dao.mapper")
@EnableFenglin
@EnableTransactionManagement
public class TestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

}
