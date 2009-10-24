package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerInfoManager;

/**
 * Prints out metrics about a certain mining
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MetricsApp {
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
	public MetricsApp(String[] args) throws MinerException {
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
		connectionArgs.put("database", values.getDb());
		connectionArgs.put("user", values.getUser());
		connectionArgs.put("password", values.getPw());

		// Set database engine
		Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

		// Get miner info
		MinerInfoManager minerInfoManager = new MinerInfoManager(connectionArgs);
		MinerInfo minerInfo = minerInfoManager.find(values.getName());
		minerInfoManager.close();		

		// Check prerequisites
		if (minerInfo == null
				|| !(minerInfo.isShiatsu() && minerInfo.isMiner())) {
			throw new MinerException(
					new Exception(
							"No mining called '"
									+ values.getName()
									+ "' found. The data must be mined before running metrics."));
		}
	}

	/**
	 * Prints metrics
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		// TODO: Metrics App
		System.out.println("TODO");
	}
	
	/**
	 * Starts frequent item set info
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		MetricsApp metrics = new MetricsApp(args);
		metrics.print();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"--user", "--login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"--password", "--pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-n", aliases = {"--name"}, usage = "set the name of the mining")
		private String name = MinerInfo.defaultName;

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
		 * Returns the name
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}
	}
}
