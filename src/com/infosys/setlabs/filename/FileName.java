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

/**
 * Maps file IDs in a database created by CVSAnaly2 to filenames/paths.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>"
 */
public class FileName {

	// Database connection
	private Connection connection;

	/**
	 * Create a new filename object
	 * 
	 * @param connection
	 *            Database connection
	 */
	public FileName(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Maps file ID id to either a filename (if nameOnly is specified) or to a
	 * path
	 * 
	 * @param id
	 * @param nameOnly
	 * @return Filename if nameOnly or path of the file
	 * @throws NoSuchFileException
	 */
	public String idToFileName(long id, boolean nameOnly)
			throws NoSuchFileException {
		return idToFileNameRecursive(id, nameOnly);
	}

	// Recursive filename method called by filename method
	private String idToFileNameRecursive(long id, boolean nameOnly)
			throws NoSuchFileException {
		// Base case
		if (id == -1) {
			return "";
		}

		Statement stmt = null;
		ResultSet rs = null;

		// Recursion
		try {
			stmt = connection.createStatement();

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
					return idToFileNameRecursive(rs.getLong("parent_id"), nameOnly)
							+ rs.getString("file_name");
				} else if (nameOnly) {
					return rs.getString("file_name");
				} else {
					return idToFileNameRecursive(rs.getLong("parent_id"), nameOnly)
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

	/**
	 * Filename application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Parse the command line arguments and options
		CommandLineValues values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("filename [options...] arguments...\n");
			System.err.println(e.getMessage() + "\n");

			// Print the list of available options
			parser.printUsage(System.err);
			System.exit(1);
		}

		// Load the properties
		Properties properties = PropertiesLoader.load("config.properties");

		Connection connection = null;

		try {
			// Get a connection to the database
			connection = ConnectionManager.getConnection(properties
					.getProperty("db.vendor"), properties
					.getProperty("db.host"), values.getDb(), values.getUser(),
					values.getPw());

			FileName fn = new FileName(connection);
			System.out.println(fn
					.idToFileName(values.getId(), values.getNameOnly()));
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		} catch (NoSuchFileException e) {
			System.out.println("NoSuchFileException: " + e.getMessage());
		} finally {
			DatabaseUtil.close(connection);
		}
	}

}
