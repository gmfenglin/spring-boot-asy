package com.feng.lin.test.demo.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import com.feng.lin.test.demo.dao.model.Test;
import com.feng.lin.test.demo.service.TestService;
import com.feng.lin.web.lib.aop.AsyControllerAspect.TaskHolder;
import com.feng.lin.web.lib.controller.ResponseMessage;
import com.feng.lin.web.lib.controller.Result;
@Component
public class Receiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
	@Autowired
	private TestService testService;
	public void receiveMessage(String message) {
		String messageId=message.substring(0, message.indexOf(":"));
		int id=Integer.parseInt(message.substring(message.indexOf(":")+1, message.length()));
		DeferredResult<Object> result=TaskHolder.messageMap.get(messageId);
		TaskHolder.messageMap.remove(messageId);
		LOGGER.info("Received <" + message + ">"+TaskHolder.messageMap.size());
		result.setResult(new Result<Test>().setSuccess(true).setCode(ResponseMessage.OK.getCode()).setData(testService.getTestById(id).get()));
		
	}
}