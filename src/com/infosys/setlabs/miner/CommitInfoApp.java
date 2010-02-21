package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.Commit;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.domain.CommitMetrics.IdType;
import com.infosys.setlabs.miner.manage.CommitManager;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerInfoManager;

/**
 * Gives information about frequent item sets
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class CommitInfoApp {
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
	public CommitInfoApp(String[] args) throws MinerException {
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
		connectionArgs.put("database", values.getDb());
		connectionArgs.put("user", values.getUser());
		connectionArgs.put("password", values.getPw());
		connectionArgs.put("server", values.getServer());
		connectionArgs.put("port", values.getPort());

		// Set database engine
		Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

		// Get miner info
		MinerInfoManager minerInfoManager = new MinerInfoManager(connectionArgs);
		MinerInfo minerInfo = minerInfoManager.find(MinerInfo.defaultName);
		minerInfoManager.close();

		// Check prerequisites
		if (minerInfo == null || !minerInfo.isShiatsu()) {
			throw new MinerException(new Exception(
					"The data must be massaged before running commit-info."));
		}
	}

	/**
	 * Prints frequent item set information
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		CommitManager commitManager = null;
		Commit commit = null;

		try {
			// Connect to the database
			commitManager = new CommitManager(connectionArgs);

			// Get commit
			commit = commitManager.find(values.getId(), values.getIdType());

			if (commit == null) {
				System.out.println("Error: Couldn't find commit with "
						+ values.getIdType() + " '" + values.getId()
						+ "' in the database.");
			} else {
				System.out.println(commit);
			}
		} finally {
			if (commitManager != null) {
				commitManager.close();
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
		CommitInfoApp commitInfo = new CommitInfoApp(args);
		commitInfo.print();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		@Argument(index = 0, usage = "name of the database to connect to", metaVar = "DATABASE", required = true)
		private String db;

		@Option(name = "-u", aliases = { "--user", "--login" }, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = { "--password", "--pw" }, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-S", aliases = { "--server" }, usage = "name of the host where database server is running (default: localhost)", metaVar = "HOSTNAME")
		private String server = "localhost";

		@Option(name = "-P", aliases = { "--port" }, usage = "port of the database server (default: 3306)", metaVar = "HOSTNAME")
		private String port = "3306";

		@Option(name = "-t", aliases = { "--type", "--id-type" }, usage = "type of the IDs (id: commit IDs (default), rev: revisions, tag: tags", metaVar = "id|rev|tag")
		private IdType idType = IdType.ID;

		@Argument(index = 1, usage = "ID of the commit", metaVar = "ID", required = true)
		private String id;

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
		 * Returns the ID type
		 * 
		 * @return idType
		 */
		public IdType getIdType() {
			return idType;
		}

		/**
		 * Returns commit ID
		 * 
		 * @return id
		 */
		public String getId() {
			return id;
		}
	}
}
