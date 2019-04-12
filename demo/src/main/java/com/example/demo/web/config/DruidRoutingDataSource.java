package com.example.demo.web.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DruidRoutingDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getJdbcType();
	}

}
