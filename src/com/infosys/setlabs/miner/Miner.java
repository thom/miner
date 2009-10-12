package com.infosys.setlabs.miner;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerManager;

/**
 * Miner
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class Miner {
	// Command line values
	private CommandLineValues values;

	// Database connection arguments
	private HashMap<String, String> connectionArgs;

	// File used to save transactions
	private File transactions;

	// File used to save frequent item sets
	private File frequentItemSets;

	// Miner Manager
	private MinerManager minerManager;

	/**
	 * Parses command line arguments, sets the database connection arguments and
	 * initializes the temporary files.
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public Miner(String[] args) throws MinerException {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("fism [options...] arguments...\n");
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

		// Initialize files
		transactions = new File(values.getDb() + ".tra");
		frequentItemSets = new File(values.getDb() + ".fis");
	}

	/**
	 * Does the frequent item set mining
	 * 
	 * @throws MinerException
	 */
	public void mine() throws MinerException {
		try {
			Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

			// Connect to MySQL database
			minerManager = new MinerManager(connectionArgs);

			System.out.println("EXEC  > fism\n");

			// TODO: switch case for mode
			format();
			apriori();
			frequentItemSets();

			System.out.println("DONE  > fism");
		} finally {
			if (minerManager != null) {
				minerManager.close();
			}
			if (!values.getKeepFiles()) {
				transactions.delete();
				frequentItemSets.delete();
			}
		}
	}

	/**
	 * Format into basket format and save in file 'transactions'
	 * 
	 * @throws MinerException
	 */
	private void format() throws MinerException {
		System.out.println("EXEC  > format");
		minerManager.format(transactions, values.getAllFiles(), false);
		System.out.println("DONE  > format\n");
	}

	/**
	 * Call apriori with the specified parameters
	 * 
	 * @throws MinerException
	 */
	private void apriori() throws MinerException {
		minerManager.apriori(values.getExec(), values.getMinSupport(), values
				.getMinItems(), transactions, frequentItemSets);
	}

	/**
	 * Parse output of apriori and save frequent item sets to the database
	 * 
	 * @throws MinerException
	 */
	private void frequentItemSets() throws MinerException {
		System.out.println("EXEC  > frequent item sets");
		minerManager.frequentItemSets(frequentItemSets);
		System.out.println("DONE  > frequent item sets\n");
	}

	/**
	 * Starts frequent item set mining
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		Miner miner = new Miner(args);
		miner.mine();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		/**
		 * Modes
		 */
		public enum Mode {
			ALL, FORMAT, APRIORI
		}

		@Option(name = "-d", aliases = {"--database", "--db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"--user", "--login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"--password", "--pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-e", aliases = {"--exec", "--executable"}, usage = "path to the executable of apriori frequent item set miner, can also be configured in conf/setup.properties")
		private String exec;

		@Option(name = "-a", aliases = {"--all", "--all-files"}, usage = "print all files affect by a transaction, including non-code files")
		private boolean allFiles = false;

		@Option(name = "-i", aliases = {"--items", "--minimal-items"}, usage = "minimal number of items per set, can also be configured in conf/setup.properties")
		private int minItems;

		@Option(name = "-s", aliases = {"--support", "--minimal-support"}, usage = "minimal support of a set (positive: percentage, negative: absolute number), can also be configured in conf/setup.properties")
		private float minSupport;

		@Option(name = "-k", aliases = {"--keep", "--keep-files"}, usage = "keep all generated temporary files")
		private boolean keepFiles = false;

		@Option(name = "-m", aliases = {"--mode"}, usage = "mode to run (all: default, f[ormat]: only formatting will happen, no frequent item set mining, a[priori]: call frequent item set miner", metaVar = "all|f|a")
		private String mode = "all";

		/**
		 * Sets default values for some of the command line arguments
		 * 
		 * @throws MinerException
		 */
		public CommandLineValues() throws MinerException {
			Properties setup = Configuration.load("setup");

			exec = setup.getProperty("apriori.exec");

			try {
				minItems = Integer.parseInt(setup
						.getProperty("apriori.minItems"));
			} catch (NumberFormatException e) {
				throw new MinerException(
						new Exception(
								"Wrong value for 'apriori.minItems' in 'setup.properties'"));
			}

			try {
				minSupport = Integer.parseInt(setup
						.getProperty("apriori.minSupport"));
			} catch (NumberFormatException e) {
				throw new MinerException(
						new Exception(
								"Wrong value for 'apriori.minSupport' in 'setup.properties'"));
			}
		}

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
		 * Returns name of the executable
		 * 
		 * @return exec
		 */
		public String getExec() {
			return exec;
		}

		/**
		 * Should all files be mined? If false, only code files are mined.
		 * 
		 * @return allFiles
		 */
		public boolean getAllFiles() {
			return allFiles;
		}

		/**
		 * Returns value of minimal number of items per set
		 * 
		 * @return minItems
		 */
		public int getMinItems() {
			return minItems;
		}

		/**
		 * Returns value of minimal support of a set (positive: percentage,
		 * negative: absolute number)
		 * 
		 * @return minSupport
		 */
		public float getMinSupport() {
			return minSupport;
		}

		/**
		 * Should all generated temporary files be kept?
		 * 
		 * @return keepFiles
		 */
		public boolean getKeepFiles() {
			return keepFiles;
		}

		public Mode getMode() {
			Mode result = null;

			if (mode.equalsIgnoreCase("all")) {
				result = Mode.ALL;
			} else if (mode.equalsIgnoreCase("format") || mode.startsWith("f")
					|| mode.startsWith("F")) {
				result = Mode.FORMAT;
			} else if (mode.equalsIgnoreCase("apriori") || mode.startsWith("a")
					|| mode.startsWith("A")) {
				result = Mode.APRIORI;
			}
			
			return result;
		}
	}
}