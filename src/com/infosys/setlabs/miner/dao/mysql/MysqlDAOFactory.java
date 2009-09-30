package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAOSession;
import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.dao.BasketFormatDAO;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.dao.FileDAO;
import com.infosys.setlabs.miner.dao.ShiatsuDAO;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * MySQL DAO Factory
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlDAOFactory extends DAOFactory {
	// Database connection information
	private String server, user, password, database;
	private int port;

	// Properties
	private Properties prop;

	public MysqlDAOFactory() {
		// Load the properties
		prop = Configuration.load("db");

		// Initialize connection parameters
		server = prop.getProperty("mysql.server");
		port = Integer.parseInt(prop.getProperty("mysql.port"));
		database = prop.getProperty("mysql.database");
		user = prop.getProperty("mysql.user");
		password = prop.getProperty("mysql.password");
	}

	private Connection getConnection() throws DataAccessException {
		try {
			// Use MySQL directly for the moment, this will be replaced by a DAO
			MysqlDataSource mds = new MysqlDataSource();

			mds.setServerName(server);
			mds.setPortNumber(port);
			mds.setDatabaseName(database);

			// Cast into a data source
			DataSource ds = (DataSource) mds;

			return ds.getConnection(user, password);
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	/**
	 * Set the connection arguments with an array
	 * 
	 * @param properties
	 */
	@Override
	public void setConnectionArgs(HashMap<String, String> connectionArgs) {
		if (connectionArgs.get("server") != null)
			setServer(connectionArgs.get("server"));
		if (connectionArgs.get("port") != null)
			setPort(Integer.parseInt(connectionArgs.get("port")));
		if (connectionArgs.get("database") != null)
			setDatabase(connectionArgs.get("database"));
		if (connectionArgs.get("user") != null)
			setUser(connectionArgs.get("user"));
		if (connectionArgs.get("password") != null)
			setPassword(connectionArgs.get("password"));
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public DAOSession getSession() throws DataAccessException {
		return new JdbcDAOSession(this.getConnection());
	}

	@Override
	public FileDAO getFileDAO(DAOSession session) throws DataAccessException {
		return new MysqlFileDAO(((JdbcDAOSession) session).getConnection());
	}
	
	@Override
	public BasketFormatDAO getBasketFormatDAO(DAOSession session) throws DataAccessException {
		return new MysqlBasketFormatDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public ShiatsuDAO getShiatsuDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlShiatsuDAO(((JdbcDAOSession) session).getConnection());		
	}	
}