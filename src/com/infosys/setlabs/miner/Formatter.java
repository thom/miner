package com.infosys.setlabs.miner;

import java.sql.Connection;
import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.DatabaseUtil;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.dao.mysql.MysqlDAOFactory;
import com.infosys.setlabs.miner.format.BasketFormat;

public class Formatter {

	/**
	 * Formatter application
	 * 
	 * @param args
	 * @throws DataAccessException
	 */
	public static void main(String[] args) throws DataAccessException {
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
			MysqlDAOFactory daoFactory = (MysqlDAOFactory) DAOFactory
					.getDAOFactory(DAOFactory.DatabaseEngine.MYSQL);
			HashMap<String, String> connectionArgs = new HashMap<String, String>();
			connectionArgs.put("database", values.getDb());
			connectionArgs.put("user", values.getUser());
			connectionArgs.put("password", values.getPw());
			daoFactory.setConnectionArgs(connectionArgs);
			connection = daoFactory.getConnection();

			BasketFormat basketFormat = new BasketFormat(connection, values
					.getAllFiles(), values.getRevs());
			basketFormat.format();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			DatabaseUtil.close(connection);
		}
	}

	private static class CommandLineValues {
		@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER")
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
