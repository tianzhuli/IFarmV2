package com.ifarm.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConDb {
	
	public static DataSource dataSource;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConDb.class);

	static {
		Properties properties = new Properties();
		InputStream inStream = ConDb.class
				.getResourceAsStream("dbcp.properties");
		try {
			properties.load(inStream);
			LOGGER.info(properties.toString());
			dataSource = BasicDataSourceFactory.createDataSource(properties);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public Connection openCon() {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
