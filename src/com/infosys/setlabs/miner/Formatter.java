package com.infosys.setlabs.miner;

import java.io.File;
import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.BasketFormatManager;
import com.infosys.setlabs.miner.manage.Manager;

/**
 * Formats revision history into a transactions file in basket format
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class Formatter {
	// Command line values
	private CommandLineValues values;

	// Database connection arguments
	private HashMap<String, String> connectionArgs;

	// File used to save transactions
	private File transactions;

	/**
	 * Parses command line arguments and sets the database connection arguments.
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
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

		// Initialize transactions file
		transactions = new File(values.getDb() + ".tra");
	}

	/**
	 * Formats revision history into basket format
	 * 
	 * @throws MinerException
	 */
	public void format() throws MinerException {
		BasketFormatManager basketFormatManager = null;

		try {
			Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

			// Connect to MySQL database
			basketFormatManager = new BasketFormatManager(connectionArgs);

			// Format
			basketFormatManager.format(transactions, values.getAllFiles(),
					values.getRevs());
		} finally {
			if (basketFormatManager != null) {
				basketFormatManager.close();
			}
		}
	}

	/**
	 * Starts the formatter
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		Formatter formatter = new Formatter(args);
		formatter.format();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
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

		/**
		 * Returns database name
		 * 
		 * @return db
		 */
		public String getDb() {
			return db;
		}

		/**
		 * Returns user name
		 * 
		 * @return user
		 */
		public String getUser() {
			return user;
		}

		/**
		 * Returns password
		 * 
		 * @return pw
		 */
		public String getPw() {
			return pw;
		}

		/**
		 * Should the revisions be written as comments into the transactions
		 * file?
		 * 
		 * @return revs
		 */
		public boolean getRevs() {
			return revs;
		}

		/**
		 * Should all files be mined? If false, only code files are mined.
		 * 
		 * @return allFiles
		 */
		public boolean getAllFiles() {
			return allFiles;
		}
	}
}
