package com.feng.lin.test.demo.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.feng.lin.test.demo.dao.dto.query.Pager;
import com.feng.lin.test.demo.dao.dto.query.TestCondition;
import com.feng.lin.test.demo.dao.mapper.TestMapper;
import com.feng.lin.test.demo.dao.model.Test;
import com.feng.lin.test.demo.service.TestService;
import com.feng.lin.web.lib.aop.AsyControllerAspect.TaskHolder;

@Service
public class TestServiceImp implements TestService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private TestMapper testMapper;

	protected void debugLog(Supplier<String> supplier) {
		if (logger.isDebugEnabled()) {
			logger.debug(supplier.get());
		}
	}

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

	@Transactional
	public Optional<Test> saveTest(Test model) {
		boolean flag = false;
		try {
			int count = testMapper.saveTest(model);
			
			if (count > 0) {
				return Optional.ofNullable(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = true;
			logger.error("saveTest", e);

		} finally {
			// 告诉忽略超时
			if(!flag) {
				synchronized (this) {
					if( !TaskHolder.local.get().isTimeout()) {
						debugLog(() -> {
							return "set no timeOut";
						});
						TaskHolder.request.get().getAsyncContext().setTimeout(-1);
					}
				}
				
			}
			// 检查超时标志,如果超时，回滚事务
			if (flag || TaskHolder.local.get().isTimeout()) {
				debugLog(() -> {
					return "rollbackOnly";
				});
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}

		}
		return Optional.empty();
	}
	@Transactional
	public int modifyTest(Test model) {
		boolean flag = false;
		try {
			int count = testMapper.modifyTest(model);
			return count;
		} catch (Exception e) {
			logger.error("modifyTest", e);
			return -1;
		}finally {
			// 告诉忽略超时
			if(!flag) {
				synchronized (this) {
					if( !TaskHolder.local.get().isTimeout()) {
						debugLog(() -> {
							return "set no timeOut";
						});
						TaskHolder.request.get().getAsyncContext().setTimeout(-1);
					}
				}
				
			}
			// 检查超时标志,如果超时，回滚事务
			if (flag || TaskHolder.local.get().isTimeout()) {
				debugLog(() -> {
					return "rollbackOnly";
				});
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}

		}
	}

}