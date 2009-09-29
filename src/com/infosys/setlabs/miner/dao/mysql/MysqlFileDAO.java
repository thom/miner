package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.miner.dao.jdbc.JdbcFileDAO;

public class MysqlFileDAO extends JdbcFileDAO {
	public MysqlFileDAO(Connection conn) {
		super(conn);
	}
}
