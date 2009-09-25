package com.infosys.setlabs.fism;

import java.sql.Connection;
import java.sql.SQLException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.fism.common.DatabaseUtil;
import com.infosys.setlabs.fism.db.ConnectionManager;
import com.infosys.setlabs.fism.format.BasketFormat;

public class Formatter {

	/**
	 * Formatter application
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
			System.err.println("formatter [options...] arguments...\n");
			System.err.println(e.getMessage() + "\n");

			// Print the list of available options
			parser.printUsage(System.err);
			System.exit(1);
		}

		Connection connection = null;

		try {
			// Get a connection to the database
			connection = ConnectionManager.getConnection(values.getDb(), values
					.getUser(), values.getPw());

			BasketFormat basketFormat = new BasketFormat(connection, values
					.getAllFiles(), values.getRevs());
			basketFormat.format();
		} catch (SQLException sqlEx) {
			System.out.println("SQLException: " + sqlEx.getMessage());
		} finally {
			DatabaseUtil.close(connection);
		}
	}

	private static class CommandLineValues {
		@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER", required = true)
		private String user;

		@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-r", aliases = {"revs", "revisions"}, usage = "print revisions in transaction file")
		private boolean revs = false;

		@Option(name = "-a", aliases = {"all", "all-files"}, usage = "print all files affect by a transaction, including non-code files")
		private boolean allFiles = false;

		public String getDb() {
			return db;
		}

		public String getUser() {
			return user;
		}

		public String getPw() {
			return pw;
		}

		public boolean getRevs() {
			return revs;
		}

		public boolean getAllFiles() {
			return allFiles;
		}
	}

}
