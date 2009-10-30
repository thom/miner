package com.infosys.setlabs.miner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerInfoManager;
import com.infosys.setlabs.miner.manage.MinerManager;

/**
 * Miner
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MinerApp {
	// Command line values
	private CommandLineValues values;

	// Database connection arguments
	private HashMap<String, String> connectionArgs;

	// File used to save transactions
	private File transactions;
	private boolean transactionsExistedBefore;

	// File used to save frequent item sets
	private File frequentItemSets;
	private boolean frequentItemSetsExistedBefore;

	// Miner Manager
	private MinerManager minerManager;

	// Miner Info
	private MinerInfoManager minerInfoManager;
	private MinerInfo minerInfo;

	// Maximum module depth
	private int maximumModuleDepth;

	/**
	 * Parses command line arguments, sets the database connection arguments and
	 * initializes the temporary files.
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public MinerApp(String[] args) throws MinerException {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("miner [options...] arguments...\n");
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

		// Initialize transactions file
		transactions = setFile(values.getTransactions(), values.getDb(), values
				.getName(), ".tra");
		transactionsExistedBefore = transactions.exists();

		// Initialize frequent item sets file
		frequentItemSets = setFile(values.getFrequentItemSets(),
				values.getDb(), values.getName(), ".fis");
		frequentItemSetsExistedBefore = frequentItemSets.exists();

		// Set database engine
		Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

		// Get miner info
		minerInfoManager = new MinerInfoManager(connectionArgs);
		MinerInfo minerInfoDefault = minerInfoManager
				.find(MinerInfo.defaultName);
		minerInfo = minerInfoManager.find(values.getName());

		// Check prerequisites
		if (minerInfoDefault == null || !minerInfoDefault.isShiatsu()) {
			minerInfoManager.close();
			throw new MinerException(new Exception(
					"The data must be massaged with "
							+ "shiatsu before running the miner."));
		}

		// Set maximum module depth
		maximumModuleDepth = minerInfoDefault.getMaximumModuleDepth();
	}
	/**
	 * Prints miner information
	 * 
	 * @throws MinerException
	 */
	public void info() throws MinerException {
		if (minerInfo == null) {
			System.out.println("No mining named '" + values.getName()
					+ "' found.");
		} else {
			System.out.println(minerInfo);
		}
	}

	/**
	 * Does the frequent item set mining
	 * 
	 * @throws MinerException
	 */
	public void mine() throws MinerException {
		try {
			// Connect to MySQL database
			minerManager = new MinerManager(connectionArgs);

			System.out.println("EXEC  > miner\n");

			// Determine what to do in which mode
			switch (values.getMode()) {
				case FORMAT :
					format();
					break;
				case APRIORI :
					format();
					apriori();
					break;
				default : // ALL
					format();
					apriori();
					frequentItemSets();
					break;
			}

			System.out.println("DONE  > miner");
		} finally {
			if (minerManager != null) {
				minerManager.close();
			}

			if (minerInfoManager != null) {
				minerInfoManager.close();
			}

			// Keep files if user specifies so
			if (!values.isKeepFiles()) {
				if (!transactionsExistedBefore) {
					transactions.delete();
				}
				if (!frequentItemSetsExistedBefore) {
					frequentItemSets.delete();
				}
			}
		}
	}

	/**
	 * Format into basket format and save in file 'transactions'
	 * 
	 * @throws MinerException
	 */
	private void format() throws MinerException {
		if (!transactionsExistedBefore || values.isOverwriteFiles()) {
			System.out.println("EXEC  > format");
			minerManager.format(transactions, values.isRevs());
			System.out.println("DONE  > format\n");
		} else {
			System.out
					.println("EXEC > format: Nothing to do, reusing existing file "
							+ transactions.getAbsolutePath() + "\n");
		}
	}

	/**
	 * Call apriori with the specified parameters
	 * 
	 * @throws MinerException
	 */
	private void apriori() throws MinerException {
		if (runApriori()) {
			try {
				frequentItemSets.createNewFile();
				System.out.println("EXEC  > apriori");
				minerManager.apriori(values.getExec(), values.getMinSupport(),
						values.getMinItems(), transactions, frequentItemSets);
				System.out.println("DONE  > apriori\n");
			} catch (IOException e) {
				throw new MinerException(e);
			}
		} else {
			System.out
					.println("EXEC > apriori: Nothing to do, reusing existing file "
							+ frequentItemSets.getAbsolutePath() + "\n");
		}
	}

	private boolean runApriori() {
		return !frequentItemSetsExistedBefore || values.isOverwriteFiles();
	}

	/**
	 * Parse output of apriori and save frequent item sets to the database
	 * 
	 * @throws MinerException
	 */
	private void frequentItemSets() throws MinerException {
		System.out.println("EXEC  > frequent item sets");

		minerManager.frequentItemSets(frequentItemSets, values.getName(),
				values.isRandomize());

		boolean create = false;

		// Update miner info
		if (minerInfo == null) {
			minerInfo = new MinerInfo();
			create = true;
		}

		if (runApriori() || !minerInfo.isMiner()
				|| minerInfo.hasRandomizedModules() != values.isRandomize()) {
			minerInfo.setName(values.getName());
			minerInfo.setShiatsu(true);
			minerInfo.setMaximumModuleDepth(maximumModuleDepth);
			minerInfo.setMiner(true);
			minerInfo.setMinimumItems(values.getMinItems());
			minerInfo.setMinimumSupport(values.getMinSupport());
			minerInfo.setRandomizedModules(values.isRandomize());

			if (create) {
				minerInfoManager.create(minerInfo);
			} else {
				minerInfoManager.update(minerInfo);
			}
		}

		System.out.println("DONE  > frequent item sets\n");
	}

	/**
	 * Creates a new file
	 * 
	 * @param fileName
	 *            file name specified as argument
	 * @param def
	 *            default file name
	 * @param name
	 *            name
	 * @param ending
	 *            ending
	 * @return File
	 */
	private File setFile(String fileName, String def, String name, String ending) {
		if (fileName != null) {
			return new File(fileName);
		} else {
			return new File(def + "_" + name + ending);
		}
	}

	/**
	 * Starts frequent item set mining
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		MinerApp minerApp = new MinerApp(args);

		if (minerApp.values.isInfo()) {
			minerApp.info();
		} else {
			minerApp.mine();
		}
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

		@Option(name = "-info", aliases = {"--info", "--information"}, usage = "show information about last mining")
		private boolean info;

		@Option(name = "-e", aliases = {"--exec", "--executable"}, usage = "path to the executable of apriori frequent item set miner, can also be configured in conf/setup.properties")
		private String exec;

		@Option(name = "-i", aliases = {"--items", "--minimum-items"}, usage = "minimum number of items per set, can also be configured in conf/setup.properties")
		private int minItems;

		@Option(name = "-s", aliases = {"--support", "--minimum-support"}, usage = "minimum support of a set (positive: percentage, negative: absolute number), can also be configured in conf/setup.properties")
		private float minSupport;

		@Option(name = "-k", aliases = {"--keep", "--keep-files"}, usage = "keep all generated files")
		private boolean keepFiles = true;

		@Option(name = "-o", aliases = {"--overwrite", "--overwrite-files"}, usage = "overwrite all generated files, even those given as input")
		private boolean overwriteFiles = false;

		@Option(name = "-v", aliases = {"--revs", "--revisions"}, usage = "add revision IDs as comments above transactions in the transactions file")
		private boolean revs = false;

		@Option(name = "-m", aliases = {"--mode"}, usage = "mode to run (all: default, format: only formatting will happen, no frequent item set mining, apriori: call frequent item set miner", metaVar = "all|format|apriori")
		private Mode mode = Mode.ALL;

		@Option(name = "-t", aliases = {"--trans", "--transactions"}, usage = "file containing transactions in basket format (if the file doesn't already exist, the miner tool creates it and writes data to it)")
		private String transactions;

		@Option(name = "-f", aliases = {"--fis", "--frequent-item-sets"}, usage = "file containing frequent item sets (if the file doesn't already exist, the miner tool creates it and writes data to it)")
		private String frequentItemSets;

		@Option(name = "-n", aliases = {"--name"}, usage = "set the name of the mining")
		private String name = MinerInfo.defaultName;

		@Option(name = "-r", aliases = {"--randomize", "--randomize-modules"}, usage = "sets random modules for files")
		private boolean randomize = false;

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
		 * Did the user request information?
		 * 
		 * @return info
		 */
		public boolean isInfo() {
			return info;
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
		 * Returns value of minimum number of items per set
		 * 
		 * @return minItems
		 */
		public int getMinItems() {
			return minItems;
		}

		/**
		 * Returns value of minimum support of a set (positive: percentage,
		 * negative: absolute number)
		 * 
		 * @return minSupport
		 */
		public float getMinSupport() {
			return minSupport;
		}

		/**
		 * Should all generated files be kept?
		 * 
		 * @return keepFiles
		 */
		public boolean isKeepFiles() {
			return keepFiles;
		}

		/**
		 * Should all generated files be overwritten (including those given as
		 * input)?
		 * 
		 * @return overwriteFiles
		 */
		public boolean isOverwriteFiles() {
			return overwriteFiles;
		}

		/**
		 * Should revision IDs be added as comments above transactions in the
		 * transactions file?
		 * 
		 * @return revs
		 */
		public boolean isRevs() {
			return revs;
		}

		/**
		 * Returns the mode
		 * 
		 * @return mode
		 */
		public Mode getMode() {
			return mode;
		}

		/**
		 * Returns the name of the file containing transactions
		 * 
		 * @return transactions
		 */
		public String getTransactions() {
			return transactions;
		}

		/**
		 * Returns the name of the file containing frequent item sets
		 * 
		 * @return frequentItemSets
		 */
		public String getFrequentItemSets() {
			return frequentItemSets;
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
		 * Should the modules be randomized?
		 * 
		 * @return randomize
		 */
		public boolean isRandomize() {
			return randomize;
		}
	}
}