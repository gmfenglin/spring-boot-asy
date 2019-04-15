package com.feng.lin.web.lib.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
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
import com.feng.lin.web.lib.controller.annotation.EnableFenglinable;

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

	public int indexOf(Object[] args, Object arg) {
		for (int i = 0; i < args.length; i++) {
			if (arg.equals(args[i])) {
				return i;
			}
		}
		return -1;
	}

	public boolean isIn(Object[] args, Object arg) {
		for (int i = 0; i < args.length; i++) {
			if (arg.equals(args[i])) {
				return true;
			}
		}
		return false;
	}

	@Around("@annotation(fenglinable)")
	public Object handleControllerOfMethod(ProceedingJoinPoint pjp, EnableFenglinable fenglinable) throws Throwable {
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
			Map<String, Class<?>[]> filedMap = new HashMap<>();
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			IntStream.range(0, parameterAnnotations.length).forEach((i) -> {
				Stream.of(parameterAnnotations[i]).filter((annotation) -> {
					return annotation.annotationType().equals(Bean.class);
				}).forEach((annotation) -> {
					Bean bean = (Bean) annotation;
					filedMap.put(args[i].getClass().getName(), bean.groups());
				});
			});
			Stream.of(args).filter((arg) -> {
				return filedMap.containsKey(arg.getClass().getName());
			}).forEach((arg) -> {
				Class<?>[] groups = filedMap.get(arg.getClass().getName());
				Field[] fields = arg.getClass().getDeclaredFields();
				for (Field field : fields) {
					List<FieldError> fieldErrors = new ArrayList<>();
					for (Class<?> group : groups) {
						Annotation[] annotations = field.getAnnotations();
						boolean flag = false;
						for (Annotation annotation : annotations) {
							Method[] methods = annotation.annotationType().getDeclaredMethods();
							for (Method methoda : methods) {
								if (methoda.getName().equals("groups")) {

									try {
										if (isIn((Object[]) methoda.invoke(annotation, new String[] {}), group)) {
											flag = true;
											break;
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								}

							}

						}
						if (!flag) {
							continue;
						}
						Set<ConstraintViolation<Object>> set = validator.validateProperty(arg, field.getName(), group);
						for (ConstraintViolation<Object> item : set) {
							FieldError fieldError = new FieldError(parameterNames[indexOf(args, arg)],
									item.getPropertyPath().toString(), item.getMessage());
							fieldErrors.add(fieldError);
						}
						if (set.size() == 0) {
							fieldErrors.clear();
							break;
						}
					}
					if (fieldErrors.size() > 0) {
						for (FieldError fieldError : fieldErrors) {
							argumentNotValidException.getFieldErrors().add(fieldError);
						}
						break;
					}

				}

			});
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
