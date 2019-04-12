package com.example.demo.web.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;

@Configuration
@PropertySource(value= {"classpath:datasource.properties"})
@EnableTransactionManagement
@MapperScan(basePackages = "com.example.demo.dao.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class DruidConfig {

	@Value("${db.datasource.mapper-locations}")
	private String mapperLocation;

	@Value("${db.datasource.config-location}")
	private String configLocation;

	@ConfigurationProperties(prefix = "db.only-read")
	@Bean(name = "onlyReadDruid")
	@Primary
	public DataSource onlyReadDruid() {
		return new DruidDataSource();
	}

	@ConfigurationProperties(prefix = "db.write-and-read")
	@Bean(name = "writeAndReadDruid")
	public DataSource writeAndReadDruid() {
		return new DruidDataSource();
	}

	// 配置Druid的监控
	// 1、配置一个管理后台的Servlet
	@Bean
	public ServletRegistrationBean statViewServlet() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
		Map<String, String> initParams = new HashMap<>();

		initParams.put("loginUsername", "admin");
		initParams.put("loginPassword", "123456");
		initParams.put("allow", "");// 默认就是允许所有访问

		bean.setInitParameters(initParams);
		return bean;
	}

	// 2、配置一个web监控的filter
	@Bean
	public FilterRegistrationBean webStatFilter() {
		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new WebStatFilter());

		Map<String, String> initParams = new HashMap<>();
		initParams.put("exclusions", "*.js,*.css,/druid/*");

		bean.setInitParameters(initParams);

		bean.setUrlPatterns(Arrays.asList("/*"));

		return bean;
	}

	@Bean
	public DataSourceTransactionManager transactionManagers() {
		return new DataSourceTransactionManager(routingDataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(routingDataSource());
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		// mybatis的XML的配置
		bean.setMapperLocations(resolver.getResources(mapperLocation));
		bean.setConfigLocation(resolver.getResource(configLocation));
		return bean.getObject();
	}

	@Bean
	public AbstractRoutingDataSource routingDataSource() {
		DruidRoutingDataSource proxy = new DruidRoutingDataSource();
		Map<Object, Object> targetDataSources = new HashMap<>(2);
		targetDataSources.put(DataSourceType.WRITE.getType(), writeAndReadDruid());
		targetDataSources.put(DataSourceType.READ.getType(), onlyReadDruid());
		proxy.setDefaultTargetDataSource(writeAndReadDruid());
		proxy.setTargetDataSources(targetDataSources);
		return proxy;
	}
}
