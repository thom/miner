package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.domain.CommitMetrics;

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

	protected String localizationSQL(int minimumCommitSize,
			int maximumCommitSize) {
		String filter = "WHERE miner_files_touched >= " + minimumCommitSize
				+ " AND " + "miner_files_touched <= " + maximumCommitSize
				+ " AND id BETWEEN ? AND ?";
		return String.format("SELECT "
				+ "@r := (SELECT COUNT(id) FROM %s %s) AS commits, "
				+ "(SUM(1 - (IF(modules_touched = 1, 0, "
				+ "(modules_touched / miner_files_touched)))) / @r) "
				+ "AS localization FROM %s %s", MysqlCommitDAO.tableName,
				filter, MysqlCommitDAO.tableName, filter);
	}

	protected String numberOfFilesMovedSQL() {
		return String.format("SELECT COUNT(*) as count "
				+ "FROM (SELECT f.id FROM %s f, file_links fl "
				+ "WHERE f.id = fl.file_id GROUP BY f.id "
				+ "HAVING COUNT(fl.parent_id) > 1) AS moved",
				MysqlMinerFileDAO.tableName);
	}

	@Override
	public CommitMetrics metrics(int startId, int stopId,
			int minimumCommitSize, int maximumCommitSize)
			throws DataAccessException {
		CommitMetrics result = new CommitMetrics();
		PreparedStatement ps = null;
		ResultSet rs = null;

		// Switch begin and end if they are reversed
		if (startId > stopId) {
			int tmp = startId;
			startId = stopId;
			stopId = tmp;
		}

		result.setStart(Integer.toString(startId));
		result.setStop(Integer.toString(stopId));

		try {
			ps = this.getConnection().prepareStatement(
					localizationSQL(minimumCommitSize, maximumCommitSize));
			ps.setInt(1, startId);
			ps.setInt(2, stopId);
			ps.setInt(3, startId);
			ps.setInt(4, stopId);
			rs = ps.executeQuery();
			while (rs.next()) {
				result.setCommits(rs.getInt("commits"));
				result.setLocalization(rs.getDouble("localization"));
				result.setFilesMoved(numberOfFilesMoved());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}

		return result;
	}

	private int numberOfFilesMoved() {
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
}
