package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

public class MysqlMinerInfoDAO extends JdbcDAO implements MinerInfoDAO {

	protected static String CREATE_MINER_INFO_TABLE = ""
			+ "CREATE TABLE miner_info ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "minimal_items INT, " + "minimal_support DOUBLE"
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS miner_info";
	protected static String SELECT_MINER_INFO_SQL = ""
			+ "SELECT id, minimal_items, minimal_support FROM miner_info";
	protected static String CREATE_MINER_INFO_SQL = ""
			+ "INSERT INTO miner_info (minimal_items, minimal_support) "
			+ "VALUES (?,?)";

	/**
	 * Creates a new miner info DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerInfoDAO(Connection conn) {
		super(conn);
	}

	@Override
	public MinerInfo get() {
		MinerInfo result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_INFO_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerInfo(rs.getInt("minimal_items"), rs
						.getDouble("minimal_support"));
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public int create(MinerInfo minerInfo) throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(CREATE_MINER_INFO_SQL,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, minerInfo.getMinimalItems());
			ps.setDouble(2, minerInfo.getMinimalSupport());
			ps.execute();

			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next())
				result = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DROP_TABLE_IF_EXISTS);
			ps.executeUpdate();
			ps.executeUpdate(CREATE_MINER_INFO_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
