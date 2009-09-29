package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.miner.dao.jdbc.JdbcBasketFormatDOA;

public class MysqlBasketFormatDAO extends JdbcBasketFormatDOA {
	public MysqlBasketFormatDAO(Connection conn) {
		super(conn);
	}
}
