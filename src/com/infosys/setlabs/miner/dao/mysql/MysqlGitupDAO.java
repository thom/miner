package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.GitupDAO;

public class MysqlGitupDAO extends JdbcDAO implements GitupDAO {
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlGitupDAO(Connection conn) {
		super(conn);
	}

	protected String createDatabaseSQL(String name) {
		return String.format("CREATE DATABASE %s", name);
	}

	protected String dropDatabaseSQL(String name) {
		return String.format("DROP DATABASE IF EXISTS %s", name);
	}

	@Override
	public void createDatabase(String name) {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(dropDatabaseSQL(name));
			ps.executeUpdate();
			ps.executeUpdate(createDatabaseSQL(name));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
