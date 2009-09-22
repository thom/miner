package com.infosys.setlabs.filename;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.infosys.setlabs.db.ConnectionManager;
import com.infosys.setlabs.util.DatabaseUtil;
import com.infosys.setlabs.util.PropertiesLoader;

public class Filename {

	private static String usage = "filename [options...] arguments...";
	private static Properties properties;

	public static String filename(Connection conn, int id, boolean nameOnly) {
		return "Foobar";
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

			filename(conn, values.getId(), values.getNameOnly());
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		} finally {
			DatabaseUtil.close(conn);
		}
	}

}
