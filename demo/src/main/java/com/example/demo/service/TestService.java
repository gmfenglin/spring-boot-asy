package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.mapper.TestMapper;
import com.example.demo.dao.model.Test;
import com.example.demo.web.config.ReadOnly;

@Service
public class TestService {

	private final TestMapper testMapper;

	@Autowired
	public TestService(TestMapper testMapper) {
		this.testMapper = testMapper;
	}

	/**
	 * 测试读取，应该使用读库
	 */
	@ReadOnly
	public List<Test> getAll() {
		return testMapper.getAll();
	}

	/**
	 * 测试读取和插入,应该使用写库
	 */
	public List<Test> testReadAndWrite() {

		this.insertOne();
		return this.getAll();
	}

	/**
	 * 测试插入数据,应该使用写库
	 */
	public Test insertOne() {
		Test test = new Test();
		test.setName("fxb");
		testMapper.insertOne(test);
		return test;
	}

	/**
	 * 测试事务能否正常工作
	 */
	@Transactional(rollbackFor = RuntimeException.class)
	public Test transInsert() {
		Test test = new Test();
		test.setName("heiheihei");
		testMapper.insertOne(test);
		throw new RuntimeException("测试事务");
	}
}