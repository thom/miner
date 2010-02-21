package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

public abstract class MysqlMinerFileMetricsDAO extends JdbcDAO {
	public MysqlMinerFileMetricsDAO(Connection conn) {
		super(conn);
	}

	protected String numberOfFilesAddedSQL(boolean allFiles) {
		return String.format("SELECT COUNT(*) as count "
				+ "FROM %s f, actions a "
				+ "WHERE a.type = 'A' AND f.id = a.file_id"
				+ (allFiles ? "" : " AND f.type = '" + MinerFile.Type.CODE
						+ "'"), MysqlMinerFileDAO.tableName);
	}

	protected String numberOfFilesMovedSQL(boolean allFiles) {
		return String.format(
				"SELECT COUNT(*) as count "
						+ "FROM (SELECT f.id FROM %s f, file_links fl "
						+ "WHERE f.id = fl.file_id "
						+ (allFiles ? "" : "AND f.type = '"
								+ MinerFile.Type.CODE + "'") + "GROUP BY f.id "
						+ "HAVING COUNT(fl.parent_id) > 1) AS moved",
				MysqlMinerFileDAO.tableName);
	}

	protected int numberOfFilesMoved(boolean allFiles) {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					numberOfFilesMovedSQL(allFiles));
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

	protected int numberOfFilesAdded(boolean allFiles) {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					numberOfFilesAddedSQL(allFiles));
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
