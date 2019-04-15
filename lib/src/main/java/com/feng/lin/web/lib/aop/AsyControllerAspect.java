package com.feng.lin.web.lib.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableValidator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.async.DeferredResult;

import com.feng.lin.web.lib.controller.annotation.Bean;
import com.feng.lin.web.lib.controller.annotation.EnFenglinable;

@Aspect
@Configuration
public class AsyControllerAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AsyncTaskExecutor asyncTaskExecutor;
	@Value("${thread-pool.timeout}")
	private long timeout;

	public AsyControllerAspect() {
	}

	protected void debugLog(Supplier<String> supplier) {
		if (logger.isDebugEnabled()) {
			logger.debug(supplier.get());
		}
	}

	protected final <T> DeferredResult<T> asyncWrap(Consumer<DeferredResult<T>> consumer) {
		DeferredResult<T> deferredResult = new DeferredResult<>(timeout);
		CompletableFuture.runAsync(() -> {
			consumer.accept(deferredResult);
		}, asyncTaskExecutor);
		return deferredResult;
	}

	private static final Validator validator = Validation.byProvider(HibernateValidator.class).configure()
			.buildValidatorFactory().getValidator();

	protected Class<?>[] determineValidationGroups(ProceedingJoinPoint invocation, Method method) {

		Validated validatedAnn = AnnotationUtils.findAnnotation(method, Validated.class);
		if (validatedAnn == null) {
			validatedAnn = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), Validated.class);
		}
		return (validatedAnn != null ? validatedAnn.value() : new Class<?>[0]);
	}

	@Around("@annotation(fenglinable)")
	public Object handleControllerOfMethod(ProceedingJoinPoint pjp, EnFenglinable fenglinable) throws Throwable {
		Object o = pjp.getThis();
		// 1. 参数解析
		Object[] args = pjp.getArgs();
		Signature signature = pjp.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		String[] parameterNames = methodSignature.getParameterNames();
		debugLog(() -> {
			return pjp.getThis().getClass().getName() + "---" + pjp.getSignature().getName() + "--"
					+ IntStream.range(0, args.length).mapToObj((i) -> {
						return parameterNames[i] + ":" + args[i];
					}).collect(Collectors.joining(","));
		});
		if (fenglinable.valid()) {
			// 2.基本类型参数验证
			ExecutableValidator execVal = validator.forExecutables();

			Method method = methodSignature.getMethod();
			ControllerMethodArgumentNotValidException argumentNotValidException = new ControllerMethodArgumentNotValidException();
			Set<ConstraintViolation<Object>> result = execVal.validateParameters(pjp.getThis(), method, pjp.getArgs(),
					determineValidationGroups(pjp, method));
			if (!result.isEmpty()) {
				for (ConstraintViolation<Object> item : result) {

					FieldError fieldError = new FieldError(pjp.getThis().getClass().getSimpleName(),
							item.getPropertyPath().toString(), item.getMessage());
					argumentNotValidException.getFieldErrors().add(fieldError);
				}
				debugLog(() -> {
					return method.getName() + "--"
							+ argumentNotValidException.getFieldErrors().stream().map((filedError) -> {
								return filedError.getObjectName() + "对象的" + filedError.getField() + "字段："
										+ filedError.getDefaultMessage();
							}).reduce("", String::concat);
				});
				throw argumentNotValidException;
			}
			// 3.对象参数验证
			Map<String, String[]> filedMap = new HashMap<>();
			Bean[] beanArray = fenglinable.beans();
			for (Bean bean : beanArray) {
				filedMap.put(bean.clsName().getName(), bean.ignoreRequire());
			}
			for (int i = 0; i < args.length; i++) {
				Object arg = args[i];
				String clsName = arg.getClass().getName();
				if (!filedMap.containsKey(clsName)) {
					continue;
				}
				Set<ConstraintViolation<Object>> validateSet = validator.validate(arg);

				String[] filedList = filedMap.get(clsName);
				for (ConstraintViolation<Object> item : validateSet) {
					boolean flag = false;
					for (String filed : filedList) {
						if (filed.equals(item.getPropertyPath().toString())) {
							Annotation annotation = item.getConstraintDescriptor().getAnnotation();
							// 4.允许为空
							if (annotation.annotationType().equals(NotNull.class)
									|| annotation.annotationType().equals(NotBlank.class)) {
								flag = true;
								break;
							}
						}
					}
					if (!flag) {
						FieldError fieldError = new FieldError(parameterNames[i], item.getPropertyPath().toString(),
								item.getMessage());
						argumentNotValidException.getFieldErrors().add(fieldError);
					}
				}

			}
			if (argumentNotValidException.getFieldErrors().size() > 0) {
				debugLog(() -> {
					return method.getName() + "--"
							+ argumentNotValidException.getFieldErrors().stream().map((filedError) -> {
								return filedError.getObjectName() + "对象的" + filedError.getField() + "字段："
										+ filedError.getDefaultMessage();
							}).reduce("", String::concat);
				});
				throw argumentNotValidException;
			}
		}
		if (fenglinable.asy()) {
			return asyncWrap((DeferredResult<Object> deferredResult) -> {
				long startTime = new Date().getTime();
				debugLog(() -> {
					return Thread.currentThread().getName() + " thread start:" + new Date().getTime();
				});
				try {
					deferredResult.setResult(pjp.proceed());
				} catch (Throwable e) {
					e.printStackTrace();
					deferredResult.setErrorResult(e);
				}
				debugLog(() -> {
					long now = new Date().getTime();
					return Thread.currentThread().getName() + " thread end:" + now + ",耗时：" + (now - startTime);
				});
			});
		} else {
			return pjp.proceed();
		}

	}

}
