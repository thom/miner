package com.infosys.setlabs.miner;

import java.util.Collection;
import java.util.HashMap;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerInfoManager;

public class MinerInfoApp {
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
	public MinerInfoApp(String[] args) throws MinerException {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("miner-info [options...] arguments...\n");
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
	}

	/**
	 * Prints frequent item set information
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		MinerInfoManager minerInfoManager = null;

		try {
			// Connect to the database
			minerInfoManager = new MinerInfoManager(connectionArgs);

			// Get miner info
			if (values.isAll()) {
				Collection<MinerInfo> minerInfos = minerInfoManager.findAll();

				if (minerInfos == null) {
					throw new MinerException(new Exception(
							"No minings found. The data must be mined before "
									+ "running miner-info."));
				}

				for (MinerInfo minerInfo : minerInfos) {
					System.out.println(minerInfo);
					System.out
							.println("-------------------------------------------------------------------------------");
				}
			} else {
				MinerInfo minerInfo = minerInfoManager.find(values.getName());

				if (minerInfo == null) {
					throw new MinerException(new Exception("No mining called '"
							+ values.getName() + "' found."
							+ " The data must be mined before "
							+ "running miner-info."));
				}

				System.out.println(minerInfo);
			}
		} finally {
			if (minerInfoManager != null) {
				minerInfoManager.close();
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
		MinerInfoApp minerInfoApp = new MinerInfoApp(args);
		minerInfoApp.print();
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

		@Option(name = "-n", aliases = {"--name"}, usage = "set the name of the mining")
		private String name = MinerInfo.defaultName;

		@Option(name = "-a", aliases = {"--all"}, usage = "show all minings")
		private boolean all = false;

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

		/**
		 * Does the user want all minings?
		 * 
		 * @return all
		 */
		public boolean isAll() {
			return all;
		}
	}
}
