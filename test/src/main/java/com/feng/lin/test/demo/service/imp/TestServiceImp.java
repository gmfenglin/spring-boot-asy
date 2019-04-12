package com.feng.lin.test.demo.service.imp;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.feng.lin.test.demo.dao.dto.query.Pager;
import com.feng.lin.test.demo.dao.dto.query.TestCondition;
import com.feng.lin.test.demo.dao.mapper.TestMapper;
import com.feng.lin.test.demo.dao.model.Test;
import com.feng.lin.test.demo.service.TestService;

@Service
public class TestServiceImp implements TestService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TestMapper testMapper;

	public Optional<Test> getTestById(Integer id) {
		try {
			return Optional.ofNullable(testMapper.getTestById(id));
		} catch (Exception e) {
			logger.error("getTestById", e);
			return Optional.empty();
		}

	}

	public int count(TestCondition condition) {
		try {
			return testMapper.count(condition);
		} catch (Exception e) {
			logger.error("pageCount", e);
			return -1;
		}

	}

	public List<Test> page(TestCondition condition, Pager pager) {
		try {
			return testMapper.page(condition, pager);
		} catch (Exception e) {
			logger.error("page", e);
			return null;
		}
	}

	public Optional<Test> saveTest(Test model) {
		try {
			int count = testMapper.saveTest(model);
			if (count > 0) {
				return Optional.ofNullable(model);
			}
		} catch (Exception e) {
			logger.error("saveTest", e);

		}
		return Optional.empty();
	}

	public int modifyTest(Test model) {
		try {
			int count = testMapper.modifyTest(model);
			return count;
		} catch (Exception e) {
			logger.error("modifyTest", e);
			return -1;
		}
	}

}