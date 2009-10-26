package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.dao.MetricsDAO;
import com.infosys.setlabs.miner.dao.BasketFormatDAO.CodeFiles;
import com.infosys.setlabs.miner.domain.MinerInfo;

/**
 * MySQL Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlMetricsDAO extends JdbcDAO implements MetricsDAO {
	private String name = MinerInfo.defaultName;
	private CodeFiles codeFiles = CodeFiles.NONE;

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
		String tmp = "SELECT (SUM(1 - (modules_touched - 1)/"
				+ "(SELECT COUNT(id) FROM miner_modules";
		String tableName = FrequentItemSetDAO.frequentItemSetsPrefix
				+ getName();

		if (codeFiles == CodeFiles.RENAMED) {
			tmp += " WHERE has_renamed_files";
		}

		return String.format(tmp + "))/(SELECT COUNT(id) FROM %s)) "
				+ "AS modularization FROM %s", tableName, tableName);
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
	public CodeFiles getCodeFiles() {
		return codeFiles;
	}

	@Override
	public void setCodeFiles(CodeFiles codeFiles) {
		this.codeFiles = codeFiles;
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
