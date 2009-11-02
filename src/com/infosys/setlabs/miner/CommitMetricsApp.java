package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.Commit;
import com.infosys.setlabs.miner.domain.CommitMetrics;
import com.infosys.setlabs.miner.domain.MinerInfo;
import com.infosys.setlabs.miner.manage.CommitManager;
import com.infosys.setlabs.miner.manage.CommitMetricsManager;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.MinerInfoManager;

/**
 * Gives information about frequent item sets
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class CommitMetricsApp {
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
	public CommitMetricsApp(String[] args) throws MinerException {
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
					"The data must be massaged before running commit-metrics."));
		}
	}

	/**
	 * Prints frequent item set information
	 * 
	 * @throws MinerException
	 */
	public void print() throws MinerException {
		CommitMetricsManager commitMetricsManager = null;
		CommitMetrics commitMetrics = null;

		try {
			// Connect to the database
			commitMetricsManager = new CommitMetricsManager(connectionArgs);

			// Get metrics
			switch (values.getIdType()) {
				case ID :
					commitMetrics = commitMetricsManager.commitMetrics(Integer
							.parseInt(values.getStart()), Integer
							.parseInt(values.getStop()));
					break;
				case REV :
					// TODO: revisions
					break;
				case TAG :
					// TODO: tags
					break;
			}

			// Print metrics
			System.out.println(commitMetrics);
		} catch (NumberFormatException e) {
			throw new MinerException(e);
		} finally {
			if (commitMetricsManager != null) {
				commitMetricsManager.close();
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
		CommitMetricsApp commitMetrics = new CommitMetricsApp(args);
		commitMetrics.print();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		/**
		 * ID types
		 */
		public enum IdType {
			ID, REV, TAG
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

		@Option(name = "-t", aliases = {"--type", "--id-type"}, usage = "type of the IDs (id: commit IDs (default), rev: revisions, tag: tags", metaVar = "id|rev|tag")
		private IdType idType = IdType.ID;

		@Argument(index = 1, usage = "ID of the commit to start with", metaVar = "START", required = true)
		private String start;

		@Argument(index = 2, usage = "ID of the commit to stop with", metaVar = "STOP", required = true)
		private String stop;

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
		 * Returns commit ID to start with
		 * 
		 * @return start
		 */
		public String getStart() {
			return start;
		}

		/**
		 * Returns commit ID to stop with
		 * 
		 * @return stop
		 */
		public String getStop() {
			return stop;
		}
	}
}
