package com.infosys.setlabs.miner;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.manage.GitupManager;
import com.infosys.setlabs.miner.manage.Manager;

/**
 * Gitup
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class GitupApp {
	// Command line values
	private CommandLineValues values;

	// Database connection arguments
	private HashMap<String, String> connectionArgs;

	// File used to save log
	private File log;
	private boolean logExistedBefore;

	// Gitup Manager
	private GitupManager gitupManager;

	/**
	 * Parses command line arguments, sets the database connection arguments and
	 * initializes the temporary files.
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public GitupApp(String[] args) throws MinerException {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("gitup [options...] arguments...\n");
			System.err.println(e.getMessage() + "\n");

			// Print the list of available options
			parser.printUsage(System.err);
			System.exit(1);
		}

		// Set connection arguments
		connectionArgs = new HashMap<String, String>();
		connectionArgs.put("user", values.getUser());
		connectionArgs.put("password", values.getPw());
		connectionArgs.put("server", values.getServer());
		connectionArgs.put("port", values.getPort());

		// Initialize transactions file
		log = setFile(values.getLog(), values.getDb(), ".log");
		logExistedBefore = log.exists();

		// Set database engine
		Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);
	}

	/**
	 * Initializes CVSAnaly2 database
	 * 
	 * @throws MinerException
	 */
	public void gitup() throws MinerException {
		try {
			// Connect to the database
			gitupManager = new GitupManager(connectionArgs);

			System.out.println("EXEC  > gitup\n");

			if (values.isShowBranches()) {
				gitupManager.showBranches(values.getRepository());
			} else {
				generateLog();
				createDatabase();
				cvsanaly();
			}

			System.out.println("DONE  > gitup");
		} finally {
			if (gitupManager != null) {
				gitupManager.close();
			}

			// Keep log file if user specifies so
			if (!values.isKeepLog()) {
				if (!logExistedBefore) {
					log.delete();
				}
			}
		}
	}

	private void generateLog() throws MinerException {
		if (runGenerateLog()) {
			System.out.println("EXEC  > git log");
			gitupManager.generateLog(values.getRepository(),
					values.getBranch(), log, false);
			System.out.println("DONE  > git log\n");
		} else {
			System.out
					.println("EXEC > git log: Nothing to do, reusing existing file "
							+ log.getAbsolutePath() + "\n");
		}
	}

	private boolean runGenerateLog() {
		return !logExistedBefore || values.isOverwriteLog();
	}

	private void createDatabase() throws MinerException {
		System.out.println("EXEC  > creating database " + values.getDb());
		gitupManager.createDatabase(values.getDb());
		System.out.println("DONE  > creating database\n");
	}

	private void cvsanaly() throws MinerException {
		System.out.println("EXEC  > cvsanaly");
		gitupManager.cvsanaly(values.getExec(), values.getDb(), log, values.getConf());
		System.out.println("DONE  > cvsanaly\n");
	}

	/**
	 * Creates a new file
	 * 
	 * @param fileName
	 *            file name specified as argument
	 * @param name
	 *            name
	 * @param ending
	 *            ending
	 * @return File
	 */
	private File setFile(String fileName, String name, String ending) {
		if (fileName != null) {
			return new File(fileName);
		} else {
			return new File(name + ending);
		}
	}

	/**
	 * Starts gitup
	 * 
	 * @param args
	 *            arguments
	 * @throws MinerException
	 */
	public static void main(String[] args) throws MinerException {
		GitupApp gitupApp = new GitupApp(args);
		gitupApp.gitup();
	}

	/**
	 * Specifies the command line values
	 * 
	 * @author Thomas Weibel <thomas_401709@infosys.com>
	 */
	private static class CommandLineValues {
		@Argument(index = 0, usage = "URI of the git repository", metaVar = "REPOSITORY", required = true)
		private String repository;

		@Option(name = "-s", aliases = {"--show, --show-branches"}, usage = "list the branches of the repository")
		private boolean showBranches;

		@Option(name = "-b", aliases = {"--branch"}, usage = "branch to use (default: master)", metaVar = "BRANCH")
		private String branch = "master";

		@Option(name = "-f", aliases = {"--config-file"}, usage = "use a custom configuration file", metaVar = "CONFIG")
		private String conf;

		@Option(name = "-d", aliases = {"--db", "--database"}, usage = "name of the database to use (default: cvsanaly)", metaVar = "DB")
		private String db = "cvsanaly";

		@Option(name = "-u", aliases = {"--user", "--login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"--password", "--pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-S", aliases = {"--server"}, usage = "name of the host where database server is running", metaVar = "HOSTNAME")
		private String server;

		@Option(name = "-P", aliases = {"--port"}, usage = "port of the database server", metaVar = "HOSTNAME")
		private String port;

		@Option(name = "-e", aliases = {"--exec", "--executable"}, usage = "path to the executable of apriori frequent item set miner, can also be configured in conf/setup.properties")
		private String exec = "cvsanaly2";

		@Option(name = "-k", aliases = {"--keep", "--keep-log"}, usage = "keep generated log")
		private boolean keepLog = true;

		@Option(name = "-o", aliases = {"--overwrite", "--overwrite-log"}, usage = "overwrite generated log")
		private boolean overwriteLog = false;

		@Option(name = "-l", aliases = {"--log", "--log-file"}, usage = "file containing git log messages (if the file doesn't already exist, the gitup tool creates it and commit messages to it)")
		private String log;

		/**
		 * Sets default values for some of the command line arguments
		 * 
		 * @throws MinerException
		 */
		public CommandLineValues() throws MinerException {
			Properties setup = Configuration.load("setup");

			// Set defaults
			exec = setup.getProperty("cvsanaly.exec");
			conf = setup.getProperty("cvsanaly.conf");
		}

		/**
		 * Returns repository URI
		 * 
		 * @return repository
		 */
		public String getRepository() {
			return repository;
		}

		/**
		 * Returns showBranches
		 * 
		 * @return showBranches
		 */
		public boolean isShowBranches() {
			return showBranches;
		}

		/**
		 * Returns branch
		 * 
		 * @return branch
		 */
		public String getBranch() {
			return branch;
		}

		/**
		 * Returns conf
		 * 
		 * @return conf
		 */
		public String getConf() {
			return conf;
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
		 * Returns name of the executable
		 * 
		 * @return exec
		 */
		public String getExec() {
			return exec;
		}

		/**
		 * Should all generated log be kept?
		 * 
		 * @return keepLog
		 */
		public boolean isKeepLog() {
			return keepLog;
		}

		/**
		 * Should the generated log be overwritten?
		 * 
		 * @return overwriteLog
		 */
		public boolean isOverwriteLog() {
			return overwriteLog;
		}

		/**
		 * Returns the name of the file containing the log
		 * 
		 * @return log
		 */
		public String getLog() {
			return log;
		}
	}
}
