package com.infosys.setlabs.formatter;

/**
 * Formats revision history transactions extracted from databases created by
 * CVSanaly2 into the market-basket format used by
 * http://www.borgelt.net/apriori.html and
 * http://www.cs.bme.hu/~bodon/en/fim_env/index.html
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class Formatter {

	public static String usage = "formatter [options...] arguments...";

	/**
	 * @param args
	 *            Database name, user and password
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
			System.err.println(usage + "\n");
			System.err.println(e.getMessage() + "\n");

			// Print the list of available options
			parser.printUsage(System.err);
			System.exit(1);
		}

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		// Get a connection to the database
		try {
			String connString = "jdbc:mysql://localhost:3306/" + values.getDb()
					+ "?" + "user=" + values.getUser();

			if (!values.getNopw()) {
				connString += "&password=" + values.getPw();
			}

			conn = DriverManager.getConnection(connString);

			try {
				stmt = conn.createStatement();

				// Build the statement
				String sqlStatement = "SELECT a.commit_id, f.id AS modified_files, f.file_name, ft.type "
						+ "FROM actions a, files f, file_types ft "
						+ "WHERE f.id = a.file_id AND f.id = ft.file_id ";
				String sqlOrder = "ORDER BY a.commit_id asc, modified_files asc;";
				if (values.getAllFiles()) {
					sqlStatement += sqlOrder;
				} else {
					sqlStatement += "AND ft.type = 'code'" + sqlOrder;
				}
				
				// Execute the query
				rs = stmt.executeQuery(sqlStatement);

				int counter = -1;

				// Retrieve the data from the result set
				rs.beforeFirst();
				while (rs.next()) {
					int commitId = rs.getInt("commit_id");

					if (counter < commitId) {
						if (counter > -1)
							System.out.print("\n");

						counter = commitId;

						if (values.getIds()) {
							System.out.println("# " + commitId);
						}
					} else {
						System.out.print(" ");
					}

					System.out.print(rs.getString("modified_files"));
				}

				// Print newline at the end
				System.out.println();
			} finally {
				// Release the resources
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException sqlEx) {
						System.out.println("SQLException: "
								+ sqlEx.getMessage());
					}
					rs = null;
				}

				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException sqlEx) {
						System.out.println("SQLException: "
								+ sqlEx.getMessage());
					}

					stmt = null;
				}

				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException sqlEx) {
						// Ignore
					}

					conn = null;
				}
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
		}
	}
}