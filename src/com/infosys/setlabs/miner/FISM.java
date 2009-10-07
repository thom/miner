package com.infosys.setlabs.miner;

import java.util.HashMap;
import java.util.Properties;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.BasketFormatManager;
import com.infosys.setlabs.miner.manage.Manager;

/**
 * FISM
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */
public class FISM {
	private CommandLineValues values;
	private HashMap<String, String> connectionArgs;

	public FISM(String[] args) throws MinerException {
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
	}

	public void fism() throws MinerException {
		// Get basket format of the transactions
		BasketFormatManager basketFormatManager = null;
		try {
			Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

			// Connect to MySQL database
			basketFormatManager = new BasketFormatManager(connectionArgs);

			// Format
			String formatterOutput = basketFormatManager.format(values
					.getAllFiles(), false);

			// TODO: write transactions into a temporary file

			// TODO: call apriori with the specified parameters

			// TODO: parse output of apriori and save frequent item sets to the
			// database
		} finally {
			if (basketFormatManager != null) {
				basketFormatManager.close();
			}

			if (!values.getKeepFiles()) {
				// TODO: cleanup (close database managers, remove temporary
				// files)
			}
		}
	}

	public static void main(String[] args) throws MinerException {
		FISM fism = new FISM(args);
		fism.fism();
	}

	private static class CommandLineValues {
		@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-e", aliases = {"exec", "executable"}, usage = "path to the executable of apriori frequent item set miner")
		private String exec;

		@Option(name = "-a", aliases = {"all", "all-files"}, usage = "print all files affect by a transaction, including non-code files")
		private boolean allFiles = false;

		@Option(name = "-m", aliases = {"minimal", "minimal-items"}, usage = "minimal number of items per set")
		private int minItems;

		@Option(name = "-s", aliases = {"support", "minimal-support"}, usage = "minimal support of a set (positive: percentage, negative: absolute number)")
		private float minSupport;

		@Option(name = "-k", aliases = {"keep", "keep-files"}, usage = "keep all generated temporary files")
		private boolean keepFiles = false;

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
		public String getDb() {
			return db;
		}

		public String getUser() {
			return user;
		}

		public String getPw() {
			return pw;
		}

		public String getExec() {
			return exec;
		}

		public void setExec(String exec) {
			this.exec = exec;
		}

		public boolean getAllFiles() {
			return allFiles;
		}

		public void setAllFiles(boolean allFiles) {
			this.allFiles = allFiles;
		}

		public int getMinItems() {
			return minItems;
		}

		public void setMinItems(int minItems) {
			this.minItems = minItems;
		}

		public float getMinSupport() {
			return minSupport;
		}

		public void setMinSupport(float minSupport) {
			this.minSupport = minSupport;
		}

		public boolean getKeepFiles() {
			return keepFiles;
		}

		public void setKeppFiles(boolean keepFiles) {
			this.keepFiles = keepFiles;
		}
	}
}