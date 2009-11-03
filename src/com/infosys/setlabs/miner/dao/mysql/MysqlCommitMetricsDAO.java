package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;

/**
 * MySQL Commit Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlCommitMetricsDAO extends JdbcDAO implements CommitMetricsDAO {
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlCommitMetricsDAO(Connection conn) {
		super(conn);
	}

	protected String modularizationSQL() {
		return String.format("SELECT (SUM(1 - (modules_touched - 1) / "
				+ "(miner_files_touched - 1)) / "
				+ "(SELECT COUNT(id) FROM %s "
				+ "WHERE miner_files_touched > 0 "
				+ "AND id BETWEEN ? AND ?)) AS modularization FROM %s "
				+ "WHERE miner_files_touched > 0 AND id BETWEEN ? AND ?",
				MysqlCommitDAO.tableName, MysqlCommitDAO.tableName);
	}

	@Override
	public double modularization(int start, int stop)
			throws DataAccessException {
		double result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		// Switch begin and end if they are reversed
		if (start > stop) {
			int tmp = start;
			start = stop;
			stop = tmp;
		}

		try {
			ps = this.getConnection().prepareStatement(modularizationSQL());
			ps.setInt(1, start);
			ps.setInt(2, stop);
			ps.setInt(3, start);
			ps.setInt(4, stop);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getDouble("modularization");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public double modularizationRevs(String startRev, String stopRev)
			throws DataAccessException {
		// TODO: Get commit ID for revs -> call modularization(start, stop)
		return 0.0;
	}

	@Override
	public double modularizationTags(String startRev, String stopRev)
			throws DataAccessException {
		// TODO: Get commit ID for tags -> call modularization(start, stop)
		return 0.0;
	}
}
