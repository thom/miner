package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

public class MysqlMinerInfoDAO extends JdbcDAO implements MinerInfoDAO {
	
	// TODO: extend to save information about moved files

	protected static String CREATE_MINER_INFO_TABLE = ""
			+ "CREATE TABLE miner_info ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "shiatsu BOOLEAN, " + "miner BOOLEAN, " + "minimal_items INT, "
			+ "minimal_support DOUBLE" + ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS miner_info";
	protected static String SELECT_MINER_INFO_SQL = ""
			+ "SELECT shiatsu, miner, minimal_items, minimal_support FROM miner_info WHERE id=1";
	protected static String CREATE_MINER_INFO_SQL = ""
			+ "INSERT INTO miner_info (id, shiatsu, miner, minimal_items, minimal_support) "
			+ "VALUES (1, false, false, 0, 0)";
	protected static String UPDATE_MINER_INFO_SQL = ""
			+ "UPDATE miner_info SET shiatsu=?, miner=?, minimal_items=?, minimal_support=? "
			+ "WHERE id=1";

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
				result = new MinerInfo();
				result.setShiatsu(rs.getBoolean("shiatsu"));
				result.setMiner(rs.getBoolean("miner"));
				result.setMinimalItems(rs.getInt("minimal_items"));
				result.setMinimalSupport(rs.getDouble("minimal_support"));
			}
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public void update(MinerInfo minerInfo) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(UPDATE_MINER_INFO_SQL);
			ps.setBoolean(1, minerInfo.isShiatsu());
			ps.setBoolean(2, minerInfo.isMiner());
			ps.setInt(3, minerInfo.getMinimalItems());
			ps.setDouble(4, minerInfo.getMinimalSupport());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public void createTables() throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DROP_TABLE_IF_EXISTS);
			ps.executeUpdate();
			ps.executeUpdate(CREATE_MINER_INFO_TABLE);
			ps.executeUpdate(CREATE_MINER_INFO_SQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
