package com.feng.lin.test.demo.dao.mapper;

import java.util.List;

import com.feng.lin.test.demo.dao.dto.query.TestCondition;
import com.feng.lin.test.demo.dao.dto.query.Pager;
import com.feng.lin.test.demo.dao.model.Test;

public interface TestMapper {

	Test getTestById(Integer Id);

	int count(TestCondition condition);

	List<Test> page(TestCondition condition,Pager pager);

	int saveTest(Test model);

	int modifyTest(Test model);

}