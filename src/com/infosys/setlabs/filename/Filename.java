package com.infosys.setlabs.filename;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.infosys.setlabs.db.ConnectionManager;
import com.infosys.setlabs.util.DatabaseUtil;
import com.infosys.setlabs.util.PropertiesLoader;

public class Filename {

	private static String usage = "filename [options...] arguments...";
	private static Properties properties;

	public static String filename(Connection conn, long id, boolean nameOnly)
			throws NoSuchFileException {
		return filenameRecursive(conn, id, nameOnly);
	}

	private static String filenameRecursive(Connection conn, long id,
			boolean nameOnly) throws NoSuchFileException {
		// Base case
		if (id == -1) {
			return "";
		}

		Statement stmt = null;
		ResultSet rs = null;

		// Recursion
		try {
			stmt = conn.createStatement();

			// Build the statement
			String sqlStatement = "SELECT f.id, f.file_name, fl.parent_id "
					+ "FROM files f, file_links fl "
					+ "WHERE f.id = fl.file_id AND f.id = " + id + ";";

			// Execute the query
			rs = stmt.executeQuery(sqlStatement);

			// Retrieve the data from the result set
			rs.beforeFirst();
			if (rs.next()) {
				if (rs.getLong("parent_id") == -1) {
					return filenameRecursive(conn, rs.getLong("parent_id"),
							nameOnly)
							+ rs.getString("file_name");
				} else if (nameOnly) {
					return rs.getString("file_name");
				} else {
					return filenameRecursive(conn, rs.getLong("parent_id"),
							nameOnly)
							+ "/" + rs.getString("file_name");
				}
			} else {
				throw new NoSuchFileException(Long.toString(id));
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
		}

		// Never reach here
		return "";
	}

	public static void main(String[] args) {
		// Parse the command line arguments and options
		CommandLineValues values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(usage + "\n");
			System.err.println(e.getMessage() + "\n");

			// Print the list of available options
			parser.printUsage(System.err);
			System.exit(1);
		}

		// Load the properties
		properties = PropertiesLoader.load("config.properties");

		Connection conn = null;

		try {
			// Get a connection to the database
			conn = ConnectionManager.getConnection(properties
					.getProperty("db.vendor"), properties
					.getProperty("db.host"), values.getDb(), values.getUser(),
					values.getPw());

			System.out.println(filename(conn, values.getId(), values
					.getNameOnly()));
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		} catch (NoSuchFileException e) {
			System.out.println("NoSuchFileException: " + e.getMessage());
		} finally {
			DatabaseUtil.close(conn);
		}
	}

}
