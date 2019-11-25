package com.linkpal.integrated.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * Description: 数据库操作工具类
 * 
 * @author lichao
 * @date 2019年6月28日 下午3:23:37
 *
 */
public class DBUtils {
	private static Connection conn = null;
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	private final static Logger LOGGER = LoggerFactory.getLogger(DBUtils.class);

	public static Connection getConnection() {
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}
		return conn;
	}

	public static void closeConnection(Connection conn, Statement st, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}

		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}

		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			LOGGER.info(e.getMessage());
		}
	}
}
