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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.infosys.setlabs.db.ConnectionManager;
import com.infosys.setlabs.util.DatabaseUtil;
import com.infosys.setlabs.util.PropertiesLoader;

public class Formatter {

	private static String usage = "formatter [options...] arguments...";
	private static Properties properties;

	public static void format(Connection conn, boolean allFiles, boolean ids) {
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();

			// Build the statement
			String sqlStatement = "SELECT a.commit_id, f.id AS modified_files, f.file_name, ft.type "
					+ "FROM actions a, files f, file_types ft "
					+ "WHERE f.id = a.file_id AND f.id = ft.file_id ";
			String sqlOrder = "ORDER BY a.commit_id asc, modified_files asc;";
			if (allFiles) {
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

					if (ids) {
						System.out.println("# " + commitId);
					}
				} else {
					System.out.print(" ");
				}

				System.out.print(rs.getString("modified_files"));
			}

			// Print newline at the end
			System.out.println();
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			DatabaseUtil.close(rs);
			DatabaseUtil.close(stmt);
		}
	}
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

		// Load the properties
		properties = PropertiesLoader.load("config.properties");

		Connection conn = null;

		try {
			// Get a connection to the database
			conn = ConnectionManager.getConnection(properties
					.getProperty("db.vendor"), properties
					.getProperty("db.host"), values.getDb(), values.getUser(),
					values.getPw());

			format(conn, values.getAllFiles(), values.getIds());
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		} finally {
			DatabaseUtil.close(conn);
		}
	}
}