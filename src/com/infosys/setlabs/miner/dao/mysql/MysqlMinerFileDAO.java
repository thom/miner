package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.miner.dao.jdbc.JdbcMinerFileDAO;

public class MysqlMinerFileDAO extends JdbcMinerFileDAO {
	public MysqlMinerFileDAO(Connection conn) {
		super(conn);
	}
}
