package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.BasketFormatManager;
import com.infosys.setlabs.miner.manage.Manager;

/**
 * Format revision history into a transactions file in basket format
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */
public class Formatter {
	private CommandLineValues values;
	private HashMap<String, String> connectionArgs;

	public Formatter(String[] args) {
		// Parse the command line arguments and options
		values = new CommandLineValues();
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

		// Set connection arguments
		connectionArgs = new HashMap<String, String>();
		connectionArgs.put("database", values.getDb());
		connectionArgs.put("user", values.getUser());
		connectionArgs.put("password", values.getPw());
	}

	public void format() throws MinerException {
		BasketFormatManager basketFormatManager = null;

		try {
			Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

			// Connect to MySQL database
			basketFormatManager = new BasketFormatManager(connectionArgs);

			// Format
			System.out.println(basketFormatManager.format(values.getAllFiles(),
					values.getRevs()));
		} finally {
			if (basketFormatManager != null) {
				basketFormatManager.close();
			}
		}
	}

	public static void main(String[] args) throws MinerException {
		Formatter formatter = new Formatter(args);
		formatter.format();
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
