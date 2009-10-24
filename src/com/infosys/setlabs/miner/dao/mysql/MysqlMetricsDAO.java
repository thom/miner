package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MetricsDAO;

// TODO: Comment
public class MysqlMetricsDAO extends JdbcDAO implements MetricsDAO {
	// TODO: Comment
	public MysqlMetricsDAO(Connection conn) {
		super(conn);
	}
	
	// TODO: MysqlMetricsDAO
}
