package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.ShiatsuManager;

/**
 * Massages the revision history data
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ShiatsuApp {
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
	public ShiatsuApp(String[] args) {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("shiatsu [options...] arguments...\n");
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
		connectionArgs.put("server", values.getServer());
		connectionArgs.put("port", values.getPort());

		// Set database engine
		Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);
	}

	/**
	 * Massage the data
	 * 
	 * @throws MinerException
	 */
	public void massage() throws MinerException {
		ShiatsuManager shiatsuManager = null;

		try {
			// Connect to the database
			shiatsuManager = new ShiatsuManager(connectionArgs);

			System.out.println("EXEC  > shiatsu\n");

			// Massage data
			shiatsuManager.massage(values.getMaxModuleDepth());

			System.out.println("DONE  > shiatsu");
		} finally {
			if (shiatsuManager != null) {
				shiatsuManager.close();
			}
		}
	}

	/**
	 * Starts the massaging process
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		ShiatsuApp shiatsuApp = new ShiatsuApp(args);
		shiatsuApp.massage();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		@Argument(index = 0, usage = "name of the database to connect to", metaVar = "DATABASE", required = true)
		private String db;

		@Option(name = "-u", aliases = {"--user", "--login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"--password", "--pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-S", aliases = {"--server"}, usage = "name of the host where database server is running (default: localhost)", metaVar = "HOSTNAME")
		private String server = "localhost";

		@Option(name = "-P", aliases = {"--port"}, usage = "port of the database server (default: 3306)", metaVar = "HOSTNAME")
		private String port = "3306";

		@Option(name = "-m", aliases = {"--max-depth", "--max-module-depth"}, usage = "sets the maximum depth of modules")
		private int maxModuleDepth = 2;

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
		 * Returns the module depth
		 * 
		 * @return moduleDepth
		 */
		public int getMaxModuleDepth() {
			return maxModuleDepth;
		}
	}
}