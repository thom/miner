package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.domain.Commit;

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
		String filter = "WHERE miner_files_touched > 1 AND id BETWEEN ? AND ?";
		return String.format("SELECT "
				+ "@r := (SELECT COUNT(id) FROM %s %s) AS commit_count, "
				+ "(SUM(1 - (modules_touched) / (miner_files_touched)) / @r) "
				+ "AS modularization FROM %s %s", MysqlCommitDAO.tableName,
				filter, MysqlCommitDAO.tableName, filter);
	}
	
	@Override
	public double modularization(int start, int stop)
			throws DataAccessException {
		// TODO: return CommitMetrics object and set number of commits
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
		MysqlCommitDAO commitDAO = new MysqlCommitDAO(this.getConnection());

		Commit startCommit = commitDAO.findByRev(startRev);
		if (startCommit == null) {
			throw new DataAccessException("No such revision '" + startRev + "'");
		}

		Commit stopCommit = commitDAO.findByRev(stopRev);
		if (stopCommit == null) {
			throw new DataAccessException("No such revision '" + stopRev + "'");
		}

		return modularization(startCommit.getId(), stopCommit.getId());
	}

	@Override
	public double modularizationTags(String startTag, String stopTag)
			throws DataAccessException {
		MysqlCommitDAO commitDAO = new MysqlCommitDAO(this.getConnection());

		Commit startCommit = commitDAO.findByTag(startTag);
		if (startCommit == null) {
			throw new DataAccessException("No such tag '" + startTag + "'");
		}

		Commit stopCommit = commitDAO.findByTag(stopTag);
		if (stopCommit == null) {
			throw new DataAccessException("No such tag '" + stopTag + "'");
		}

		return modularization(startCommit.getId(), stopCommit.getId());
	}
}
