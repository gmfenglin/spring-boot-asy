package com.feng.lin.test.demo.service;

import java.util.List;
import java.util.Optional;

import com.feng.lin.test.demo.dao.dto.query.Pager;
import com.feng.lin.test.demo.dao.dto.query.TestCondition;
import com.feng.lin.test.demo.dao.model.Test;

public interface TestService {

	Optional<Test> getTestById(Integer id);

	int count(TestCondition condition);

	List<Test> page(TestCondition condition,Pager pager);

	Optional<Test> saveTest(Test model);

	int modifyTest(Test model);

}