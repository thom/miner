package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.miner.dao.jdbc.JdbcShiatsuDAO;

public class MysqlShiatsuDAO extends JdbcShiatsuDAO {
	public MysqlShiatsuDAO(Connection conn) {
		super(conn);
	}
}
