package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetMetricsDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSetMetrics;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * MySQL FrequentItemSetMetrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlFrequentItemSetMetricsDAO extends JdbcDAO
		implements
			FrequentItemSetMetricsDAO {
	private String name = MinerInfo.defaultName;

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlFrequentItemSetMetricsDAO(Connection conn) {
		super(conn);
	}

	protected String localizationSQL() {
		String tableName = MysqlFrequentItemSetDAO.frequentItemSetsPrefix
				+ getName();
		return String.format("SELECT "
				+ "@r := (SELECT COUNT(id) FROM %s) AS fis_count, "
				+ "(SUM(1 - (IF(modules_touched = 1, 0, "
				+ "(modules_touched / size)))) / @r) "
				+ "AS localization FROM %s", tableName, tableName);
	}

	protected String numberOfFilesMovedSQL() {
		return String.format("SELECT COUNT(*) as count "
				+ "FROM (SELECT f.id FROM %s f, file_links fl "
				+ "WHERE f.id = fl.file_id GROUP BY f.id "
				+ "HAVING COUNT(fl.parent_id) > 1) AS moved",
				MysqlMinerFileDAO.tableName);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public FrequentItemSetMetrics metrics() {
		FrequentItemSetMetrics result = new FrequentItemSetMetrics();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(localizationSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				result.setLocalization(rs.getDouble("localization"));
				result.setFrequentItemSets(rs.getInt("fis_count"));
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
