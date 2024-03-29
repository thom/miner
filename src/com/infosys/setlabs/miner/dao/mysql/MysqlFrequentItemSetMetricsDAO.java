package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.miner.dao.FrequentItemSetMetricsDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSetMetrics;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * MySQL FrequentItemSetMetrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlFrequentItemSetMetricsDAO extends MysqlMinerFileMetricsDAO
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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public FrequentItemSetMetrics metrics(boolean allFiles) {
		FrequentItemSetMetrics result = new FrequentItemSetMetrics();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(localizationSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				result.setLocalization(rs.getDouble("localization"));
				result.setFrequentItemSets(rs.getInt("fis_count"));
				result.setFilesAdded(numberOfFilesAdded(allFiles));
				result.setFilesMoved(numberOfFilesMoved(allFiles));
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
