package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.RepositoryFile;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerFileManager;
import com.infosys.setlabs.miner.manage.RepositoryFileManager;

/**
 * Gives information about files
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class FileInfoApp {
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
	public FileInfoApp(String[] args) {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("file-info [options...] arguments...\n");
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
	 * Prints file information
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		MinerFileManager minerFileManager = null;
		RepositoryFileManager repositoryFileManager = null;
		RepositoryFile file = null;

		try {
			// Connect to MySQL database
			minerFileManager = new MinerFileManager(connectionArgs);

			// Get file path
			file = minerFileManager.find(values.getId());

			if (file == null) {
				// Connect to MySQL database
				repositoryFileManager = new RepositoryFileManager(
						connectionArgs);

				// Get file path
				file = repositoryFileManager.find(values.getId());
			}

			if (file == null) {
				System.out.println("Error: Couldn't find file with ID '"
						+ values.getId() + "' in the database.");
			} else {
				System.out.println(file);
			}
		} finally {
			if (minerFileManager != null) {
				minerFileManager.close();
			}
		}
	}
	/**
	 * Starts the ID to file name mapper
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		FileInfoApp fileInfoApp = new FileInfoApp(args);
		fileInfoApp.print();
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

		@Option(name = "-i", aliases = {"--id"}, usage = "ID of the file", required = true)
		private int id;

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
		 * Returns file ID
		 * 
		 * @return id
		 */
		public int getId() {
			return id;
		}
	}
}
