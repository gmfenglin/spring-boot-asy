package com.example.demo.web.controller;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.model.Test;
import com.example.demo.service.TestService;
import com.feng.lin.web.lib.controller.annotation.Bean;
import com.feng.lin.web.lib.controller.annotation.Beans;
import com.feng.lin.web.lib.controller.annotation.EnAsyable;

@RequestMapping("/demo")
@RestController
public class DemoController  {
	@Autowired
	private TestService testService;
	@GetMapping("/{id}")
	@Beans(beans = { @Bean(clsName = Test.class, ignoreRequire = {"testId"}) })
	@EnAsyable
	public Object hello(@PathVariable @Max(12)
	@Min(1) Integer id, Test test) {
		return "hello world"+id;
	}
	@GetMapping
	@EnAsyable
	public Object getAll() {
		return testService.getAll();
	}
	@GetMapping("/insertOne")
	@EnAsyable
	public Object insertOne() {
		return testService.insertOne();
	}

	@GetMapping("/readAndWrite")
	@EnAsyable
	public Object readAndWrite() {
		return testService.testReadAndWrite();
	}

	@GetMapping("/transInsert")
	@EnAsyable
	public Object transInsert() {
		return testService.transInsert();
	}
}
