package com.infosys.setlabs.miner;

import java.sql.Connection;
import java.sql.SQLException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.DatabaseUtil;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.dao.mysql.MysqlDAOFactory;
import com.infosys.setlabs.miner.db.ConnectionManager;
import com.infosys.setlabs.miner.filename.FileName;
import com.infosys.setlabs.miner.filename.NoSuchFileException;

/**
 * Maps file IDs in a database created by CVSAnaly2 to filenames/paths.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>"
 */
public class IdToFileName {

	/**
	 * ID to filename mapper
	 * 
	 * @param args
	 * @throws DataAccessException 
	 */
	public static void main(String[] args) throws DataAccessException {
		// Parse the command line arguments and options
		CommandLineValues values = new CommandLineValues();
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

		Connection connection = null;

		try {
			// Get a connection to the database
			MysqlDAOFactory daoFactory = (MysqlDAOFactory) DAOFactory.getDAOFactory(DAOFactory.DatabaseEngine.MYSQL);
			String[] connectionArgs = {values.getDb(), values.getUser(), values.getPw()};
			daoFactory.setConnectionArgs(connectionArgs);
			connection = daoFactory.getConnection();

			FileName fn = new FileName(connection);
			System.out.println(fn.idToFileName(values.getId(), values
					.getNameOnly()));
		} catch (NoSuchFileException e) {
			System.out.println("NoSuchFileException: " + e.getMessage());
		} finally {
			DatabaseUtil.close(connection);
		}
	}

	private static class CommandLineValues {
		@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
		private String db;

		@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER", required = true)
		private String user;

		@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
		private String pw;

		@Option(name = "-i", aliases = {"id"}, usage = "ID of the file", required = true)
		private long id;

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

		public long getId() {
			return id;
		}

		public boolean getNameOnly() {
			return nameOnly;
		}
	}

}
