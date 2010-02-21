package com.infosys.setlabs.miner;

import java.util.ArrayList;
import java.util.HashMap;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.CommitMetrics;
import com.infosys.setlabs.miner.manage.CommitMetricsManager;
import com.infosys.setlabs.miner.manage.Manager;

/**
 * Gives information about frequent item sets
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class CommitMetricsApp {
	// Command line values
	private CommandLineValues values;

	// Database connection arguments
	private HashMap<String, String> connectionArgs;

	/**
	 * Parses command line arguments and sets the database connection arguments.
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public CommitMetricsApp(String[] args) throws MinerException {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("commit-info [options...] arguments...\n");
			System.err.println(e.getMessage() + "\n");

			// Print the list of available options
			parser.printUsage(System.err);
			System.exit(1);
		}

		// Set connection arguments
		connectionArgs = new HashMap<String, String>();
		connectionArgs.put("user", values.getUser());
		connectionArgs.put("password", values.getPw());
		connectionArgs.put("server", values.getServer());
		connectionArgs.put("port", values.getPort());

		// Set database engine
		Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);
	}

	/**
	 * Prints frequent item set information
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		CommitMetricsManager commitMetricsManager = null;

		try {
			// Connect to the database
			commitMetricsManager = new CommitMetricsManager(connectionArgs);

			// Get commit metrics
			for (CommitMetrics cm : commitMetricsManager.commitMetrics(values
					.getDatabases(), values.isAllFiles(), values
					.getMinCommitSize(), values.getMaxCommitSize())) {
				cm.setCSV(values.isCSV());
				System.out.println(cm);
				if (!values.isCSV()) {
					System.out
							.println("-------------------------------------------------------------------------------");
				}
			}
		} finally {
			if (commitMetricsManager != null) {
				commitMetricsManager.close();
			}
		}
	}

	/**
	 * Starts frequent item set info
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		CommitMetricsApp commitMetrics = new CommitMetricsApp(args);
		commitMetrics.print();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		@Argument(index = 0, usage = "databases to get metrics for", metaVar = "DATABASE1 [DATABASE2...]", required = true)
		private ArrayList<String> databases;

		@Option(name = "-u", aliases = { "--user", "--login" }, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = { "--password", "--pw" }, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-S", aliases = { "--server" }, usage = "name of the host where database server is running (default: localhost)", metaVar = "HOSTNAME")
		private String server = "localhost";

		@Option(name = "-P", aliases = { "--port" }, usage = "port of the database server (default: 3306)", metaVar = "HOSTNAME")
		private String port = "3306";

		@Option(name = "-c", aliases = { "--csv" }, usage = "should the output be comma separated values?")
		private boolean csv = false;

		@Option(name = "-a", aliases = { "--all", "--all-files" }, usage = "use all files for mining (default: only code files)")
		private boolean allFiles = false;

		@Option(name = "-mic", aliases = { "--min-commit-size" }, usage = "minimum size of commits (number of code files) added to the transactions file (has to be >= 2, default: 2)")
		private int minCommitSize = 2;

		@Option(name = "-mc", aliases = { "--max-commit-size" }, usage = "maximum size of commits (number of code files) added to the transactions file (-1: no limit (default))")
		private int maxCommitSize = 50;

		/**
		 * Returns databases
		 * 
		 * @return databases
		 */
		public ArrayList<String> getDatabases() {
			return databases;
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
		 * Returns server
		 * 
		 * @return server
		 */
		public String getServer() {
			return server;
		}

		/**
		 * Returns port
		 * 
		 * @return port
		 */
		public String getPort() {
			return port;
		}

		/**
		 * Does the user want comma separated values as output?
		 * 
		 * @return csv
		 */
		public boolean isCSV() {
			return csv;
		}

		/**
		 * Does the user want the metrics for all files?
		 * 
		 * @return allFiles
		 */
		public boolean isAllFiles() {
			return allFiles;
		}

		/**
		 * Minimum size of commits (number of code files) added to the
		 * transactions file (has to be >= 2)
		 * 
		 * @return minCommitSize
		 */
		public int getMinCommitSize() {
			return minCommitSize;
		}

		/**
		 * Maximum size of commits (number of code files) added to the
		 * transactions file
		 * 
		 * @return maxCommitSize
		 */
		public int getMaxCommitSize() {
			return maxCommitSize;
		}
	}
}
