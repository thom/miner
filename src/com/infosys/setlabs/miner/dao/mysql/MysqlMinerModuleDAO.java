package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.miner.dao.jdbc.JdbcMinerModuleDAO;

public class MysqlMinerModuleDAO extends JdbcMinerModuleDAO {
	public MysqlMinerModuleDAO(Connection conn) {
		super(conn);
	}
}
