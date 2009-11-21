package com.infosys.setlabs.miner;

import java.util.ArrayList;
import java.util.HashMap;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.FrequentItemSetMetrics;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.manage.FrequentItemSetMetricsManager;
import com.infosys.setlabs.miner.manage.Manager;

/**
 * Prints out metrics about a certain mining
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FrequentItemSetMetricsApp {
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
	public FrequentItemSetMetricsApp(String[] args) throws MinerException {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("metrics [options...] arguments...\n");
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
	 * Prints metrics
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		FrequentItemSetMetricsManager frequentItemSetMetricsManager = null;

		try {
			// Get new frequent item set metrics manager
			frequentItemSetMetricsManager = new FrequentItemSetMetricsManager(
					connectionArgs);
			frequentItemSetMetricsManager.setName(values.getName());

			// Get frequent item set metrics
			for (FrequentItemSetMetrics fism : frequentItemSetMetricsManager
					.frequentItemSetMetrics(values.getMinings())) {
				fism.setCSV(values.isCSV());
				System.out.println(fism);
				if (!values.isCSV()) {
					System.out
							.println("-------------------------------------------------------------------------------");
				}
			}
		} finally {
			if (frequentItemSetMetricsManager != null) {
				frequentItemSetMetricsManager.close();
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
		FrequentItemSetMetricsApp metrics = new FrequentItemSetMetricsApp(args);
		metrics.print();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		@Argument(index = 0, usage = "minings to get metrics for", metaVar = "DATABASE1:MINING1 [DATABASE2:MINING2...]", required = true)
		private ArrayList<String> minings;

		@Option(name = "-u", aliases = {"--user", "--login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"--password", "--pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-S", aliases = {"--server"}, usage = "name of the host where database server is running (default: localhost)", metaVar = "HOSTNAME")
		private String server = "localhost";

		@Option(name = "-P", aliases = {"--port"}, usage = "port of the database server (default: 3306)", metaVar = "HOSTNAME")
		private String port = "3306";

		@Option(name = "-n", aliases = {"--name"}, usage = "set the name of the mining")
		private String name = MinerInfo.defaultName;

		@Option(name = "-c", aliases = {"--csv"}, usage = "should the output be comma separated values?")
		private boolean csv = false;

		/**
		 * Returns minings
		 * 
		 * @return minings
		 */
		public ArrayList<String> getMinings() {
			return minings;
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
		 * Returns the name
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Does the user want comma separated values as output?
		 * 
		 * @return csv
		 */
		public boolean isCSV() {
			return csv;
		}
	}
}
