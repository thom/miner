package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MetricsDAO;

/**
 * MySQL Metrics DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlMetricsDAO extends JdbcDAO implements MetricsDAO {
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMetricsDAO(Connection conn) {
		super(conn);
	}

	@Override
	public double modularization(boolean hasRenamedFiles) {
		// TODO: modularization metrics
		return 0;
	}
}
