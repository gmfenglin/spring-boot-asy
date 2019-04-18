package com.feng.lin.test.demo.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.validation.constraints.Min;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feng.lin.test.demo.dao.dto.query.Pager;
import com.feng.lin.test.demo.dao.dto.query.TestCondition;
import com.feng.lin.test.demo.dao.model.Test;
import com.feng.lin.test.demo.dao.model.Test.ModifiedNotNull;
import com.feng.lin.test.demo.dao.model.Test.ModifiedNull;
import com.feng.lin.test.demo.dao.model.Test.Save;
import com.feng.lin.test.demo.service.TestService;
import com.feng.lin.web.lib.aop.AsyControllerAspect.TaskHolder;
import com.feng.lin.web.lib.controller.ResponseMessage;
import com.feng.lin.web.lib.controller.Result;
import com.feng.lin.web.lib.controller.annotation.Bean;
import com.feng.lin.web.lib.controller.annotation.EnableFenglinable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/test")
@Api("/test")
@RestController
public class TestController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TestService testService;

	@Autowired
	private StringRedisTemplate template;
	protected void debugLog(Supplier<String> supplier) {
		if (logger.isDebugEnabled()) {
			logger.debug(supplier.get());
		}
	}

	@GetMapping("/message/{id}")
	@EnableFenglinable(setResult = false)
	@ApiOperation("message")
	public Object message(@PathVariable @Min(1) Integer id) {
		String messageId = Thread.currentThread().getId() + "-" + System.currentTimeMillis();
		TaskHolder.messageMap.put(messageId, TaskHolder.deferredResult.get());
		template.convertAndSend("mytopic", messageId + ":" + id);
		debugLog(()->"send message.");
		return TaskHolder.deferredResult.get();
	}

	@GetMapping("/{id}")
	@EnableFenglinable
	@ApiOperation("getById")
	public Object getById(@PathVariable @Min(1) Integer id) {
		Optional<Test> test = testService.getTestById(id);
		if (test.isPresent()) {
			return new Result<Test>().setSuccess(true).setCode(ResponseMessage.OK.getCode()).setData(test.get());
		} else {
			return new Result<Test>().setSuccess(true).setCode(ResponseMessage.NODATA.getCode())
					.setMessage(ResponseMessage.NODATA.getMessage());
		}

	}

	@GetMapping
	@EnableFenglinable
	@ApiOperation("getByCondition")
	public Object getByCondition(TestCondition condition, Pager pager) {
		int count = testService.count(condition);
		if (count > 0) {
			Map<String, Object> map = new HashMap(2);
			map.put("count", count);
			List<Test> testList = testService.page(condition, pager);
			map.put("list", testList);
			return new Result<Map<String, Object>>().setSuccess(true).setCode(ResponseMessage.OK.getCode())
					.setData(map);
		} else {
			return new Result<Map<String, Object>>().setSuccess(true).setCode(ResponseMessage.NODATA.getCode())
					.setMessage(ResponseMessage.NODATA.getMessage());
		}

	}

	@PostMapping
	@EnableFenglinable
	@ApiOperation("save")
	public Object save(@Bean(groups = { Save.class }) Test test) {
		Optional<Test> testOptional = testService.saveTest(test);
		if (testOptional.isPresent()) {
			return new Result<Test>().setSuccess(true).setCode(ResponseMessage.OK.getCode())
					.setData(testOptional.get());
		} else {
			return new Result<Test>().setSuccess(true).setCode(ResponseMessage.FAIL.getCode())
					.setMessage(ResponseMessage.FAIL.getMessage());
		}
	}

	@PutMapping("/{id}")
	@EnableFenglinable
	@ApiOperation("modify")
	public Object modify(@Bean(groups = { ModifiedNull.class, ModifiedNotNull.class }) Test test) {
		int count = testService.modifyTest(test);
		if (count > 0) {
			return new Result<Integer>().setSuccess(true).setCode(ResponseMessage.OK.getCode()).setData(count);
		} else {
			return new Result<Integer>().setSuccess(true).setCode(ResponseMessage.FAIL.getCode()).setData(count)
					.setMessage(ResponseMessage.FAIL.getMessage());
		}

	}

}