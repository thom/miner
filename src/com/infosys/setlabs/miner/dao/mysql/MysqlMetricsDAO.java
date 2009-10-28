package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.dao.MetricsDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * MySQL Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlMetricsDAO extends JdbcDAO implements MetricsDAO {
	private String name = MinerInfo.defaultName;

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMetricsDAO(Connection conn) {
		super(conn);
	}

	protected String modularizationSQL() {
		String tableName = FrequentItemSetDAO.frequentItemSetsPrefix
				+ getName();
		
		// TODO: Fix this!
		return String.format("SELECT (SUM(1 - (modules_touched - 1)/"
				+ "(SELECT COUNT(id) FROM miner_modules))/"
				+ "(SELECT COUNT(id) FROM %s)) AS modularization FROM %s",
				tableName, tableName);
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
	public double modularization() {
		double result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(modularizationSQL());
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
}
