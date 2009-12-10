package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.domain.CommitMetrics;

/**
 * MySQL Commit Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlCommitMetricsDAO extends MysqlMinerFileMetricsDAO
		implements
			CommitMetricsDAO {
	private int minimumCommitSize;
	private int maximumCommitSize;

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlCommitMetricsDAO(Connection conn) {
		super(conn);
	}

	protected String localizationSQL() {
		String filter = "WHERE miner_files_touched >= " + minimumCommitSize
				+ " AND miner_files_touched <= " + maximumCommitSize;
		return String.format("SELECT "
				+ "@r := (SELECT COUNT(id) FROM %s %s) AS commits, "
				+ "(SUM(1 - (IF(modules_touched = 1, 0, "
				+ "(modules_touched / miner_files_touched)))) / @r) "
				+ "AS localization FROM %s %s", MysqlCommitDAO.tableName,
				filter, MysqlCommitDAO.tableName, filter);
	}

	@Override
	public int getMinimumCommitSize() {
		return minimumCommitSize;
	}

	@Override
	public void setMinimumCommitSize(int minimumCommitSize) {
		this.minimumCommitSize = minimumCommitSize;
	}

	@Override
	public int getMaximumCommitSize() {
		return maximumCommitSize;
	}

	@Override
	public void setMaximumCommitSize(int maximumCommitSize) {
		this.maximumCommitSize = maximumCommitSize;
	}

	@Override
	public CommitMetrics metrics() throws DataAccessException {
		CommitMetrics result = new CommitMetrics();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(localizationSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				result.setCommits(rs.getInt("commits"));
				result.setLocalization(rs.getDouble("localization"));
				result.setFilesAdded(numberOfFilesAdded());
				result.setFilesMoved(numberOfFilesMoved());
				result.setMinimumCommitSize(minimumCommitSize);
				result.setMaximumCommitSize(maximumCommitSize);
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
