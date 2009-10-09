package com.infosys.setlabs.miner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.common.ExecWrapper;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.BasketFormatManager;
import com.infosys.setlabs.miner.manage.FrequentItemSetManager;
import com.infosys.setlabs.miner.manage.Manager;

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
	public void fism() throws MinerException {
		try {
			System.out.println("EXEC  > fism\n");

			// Format into basket format and save in file 'transactions'
			System.out.println("EXEC  > format");
			format();
			System.out.println("DONE  > format\n");

			// Call apriori with the specified parameters
			apriori();

			// Parse output of apriori and save frequent item sets to the
			// database
			System.out.println("EXEC  > frequent item sets");
			frequentItemSets();
			System.out.println("DONE  > frequent item sets\n");

			System.out.println("DONE  > fism");
		} finally {
			if (!values.getKeepFiles()) {
				transactions.delete();
				frequentItemSets.delete();
			}
		}
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

			// Format and write transactions to a file
			basketFormatManager.format(transactions, values.getAllFiles(),
					false);
		} finally {
			if (basketFormatManager != null) {
				basketFormatManager.close();
			}
		}
	}

	/**
	 * Calls <code>apriori</code> frequent item set miner
	 * 
	 * @throws MinerException
	 */
	public void apriori() throws MinerException {
		String[] cmd = {values.getExec(), "-s" + values.getMinSupport(),
				"-m" + values.getMinItems(), "-v:%a %4S",
				this.transactions.getAbsolutePath(),
				this.frequentItemSets.getAbsolutePath()};
		ExecWrapper apriori = new ExecWrapper(cmd);
		apriori.run();
	}

	/**
	 * Writes frequent item sets to the database
	 * 
	 * @throws MinerException
	 */
	public void frequentItemSets() throws MinerException {
		FrequentItemSetManager frequentItemSetManager = null;

		try {
			Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

			// Connect to MySQL database
			frequentItemSetManager = new FrequentItemSetManager(connectionArgs);

			// Create tables
			frequentItemSetManager.createTables();

			// Iterate over output of apriori and add frequent item sets
			// to the database
			BufferedReader in = new BufferedReader(new FileReader(
					frequentItemSets));
			String line;
			while ((line = in.readLine()) != null) {
				frequentItemSetManager.create(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (frequentItemSetManager != null) {
				frequentItemSetManager.close();
			}
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
		Miner miner = new Miner(args);
		miner.fism();
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

		@Option(name = "-e", aliases = {"exec", "executable"}, usage = "path to the executable of apriori frequent item set miner, can also be configured in conf/setup.properties")
		private String exec;

		@Option(name = "-a", aliases = {"all", "all-files"}, usage = "print all files affect by a transaction, including non-code files")
		private boolean allFiles = false;

		@Option(name = "-m", aliases = {"minimal", "minimal-items"}, usage = "minimal number of items per set, can also be configured in conf/setup.properties")
		private int minItems;

		@Option(name = "-s", aliases = {"support", "minimal-support"}, usage = "minimal support of a set (positive: percentage, negative: absolute number), can also be configured in conf/setup.properties")
		private float minSupport;

		@Option(name = "-k", aliases = {"keep", "keep-files"}, usage = "keep all generated temporary files")
		private boolean keepFiles = false;

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
	}
}