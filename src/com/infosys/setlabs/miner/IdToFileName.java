package com.infosys.setlabs.miner;

import java.util.HashMap;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.domain.RepositoryFile;
import com.infosys.setlabs.miner.manage.Manager;
import com.infosys.setlabs.miner.manage.RepositoryFileManager;

/**
 * Maps file IDs in a database created by CVSAnaly2 to filenames/paths.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>"
 */
public class IdToFileName {
	private CommandLineValues values;
	private HashMap<String, String> connectionArgs;

	public IdToFileName(String[] args) {
		// Parse the command line arguments and options
		values = new CommandLineValues();
		CmdLineParser parser = new CmdLineParser(values);

		// Set width of the error display area
		parser.setUsageWidth(80);

		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println("id2filename [options...] arguments...\n");
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

	public String getFileName() throws MinerException {
		RepositoryFileManager repositoryFileManager = null;

		try {
			Manager.setCurrentDatabaseEngine(DAOFactory.DatabaseEngine.MYSQL);

			// Connect to MySQL database
			repositoryFileManager = new RepositoryFileManager(connectionArgs);

			// Get file path
			RepositoryFile repositoryFile = repositoryFileManager.find(values
					.getId());
			if (values.getNameOnly()) {
				return repositoryFile.getFileName();
			} else {
				return repositoryFile.getPath();
			}
		} finally {
			if (repositoryFileManager != null) {
				repositoryFileManager.close();
			}
		}
	}

	public static void main(String[] args) throws MinerException {
		IdToFileName idToFileName = new IdToFileName(args);
		System.out.println(idToFileName.getFileName());
	}

	private static class CommandLineValues {
		@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER")
		private String user;

		@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-i", aliases = {"id"}, usage = "ID of the file", required = true)
		private int id;

		@Option(name = "-n", aliases = {"name", "nameonly"}, usage = "get only the name and not the path of the file")
		private Boolean nameOnly = false;

		public String getDb() {
			return db;
		}

		public String getUser() {
			return user;
		}

		public String getPw() {
			return pw;
		}

		public int getId() {
			return id;
		}

		public boolean getNameOnly() {
			return nameOnly;
		}
	}
}
