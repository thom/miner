package com.infosys.setlabs.util;

import java.sql.*;

/**
 * Provides some basic methods for handling JDBC tasks such as closing a
 * ResultSet, PreparedStatement, ...
 * 
 * @author "Thomas Weibel <thom@ikhono.net>"
 */
public class DatabaseUtil {

	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		}
	}

	public static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		}
	}

	public static void close(PreparedStatement ps) {
		try {
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		}
	}

	public static void close(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		}
	}

	public static java.sql.Date getJavaSqlDate() {

		java.util.Date javaUtilDate = new java.util.Date();
		return new java.sql.Date(javaUtilDate.getTime());
	}

	public static String getTrimmedString(ResultSet resultSet, int index)
			throws SQLException {

		String value = resultSet.getString(index);

		if (value != null) {
			value = value.trim();
		}

		return value;
	}

	public static String getTrimmedString(ResultSet resultSet, String columnName)
			throws SQLException {

		String value = resultSet.getString(columnName);

		if (value != null) {
			value = value.trim();
		}

		return value;
	}

}
