package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;

public abstract class MysqlMinerFileMetricsDAO extends JdbcDAO {
	public MysqlMinerFileMetricsDAO(Connection conn) {
		super(conn);
	}

	protected String numberOfFilesAddedSQL() {
		return String.format("SELECT COUNT(*) as count "
				+ "FROM %s f, actions a "
				+ "WHERE a.type = 'A' AND f.id = a.file_id",
				MysqlMinerFileDAO.tableName);
	}

	protected String numberOfFilesMovedSQL() {
		return String.format("SELECT COUNT(*) as count "
				+ "FROM (SELECT f.id FROM %s f, file_links fl "
				+ "WHERE f.id = fl.file_id GROUP BY f.id "
				+ "HAVING COUNT(fl.parent_id) > 1) AS moved",
				MysqlMinerFileDAO.tableName);
	}

	protected int numberOfFilesMoved() {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(numberOfFilesMovedSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	protected int numberOfFilesAdded() {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(numberOfFilesAddedSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}
}
