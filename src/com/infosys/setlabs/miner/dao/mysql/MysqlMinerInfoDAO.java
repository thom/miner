package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

public class MysqlMinerInfoDAO extends JdbcDAO implements MinerInfoDAO {

	// TODO: clean up the mess		
	protected static String CREATE_MINER_INFO_TABLE = ""
			+ "CREATE TABLE miner_info ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "name VARCHAR(255) NOT NULL DEFAULT '', " + "shiatsu BOOLEAN, "
			+ "miner BOOLEAN, " + "included_files VARCHAR(255), "
			+ "minimal_items INT, " + "minimal_support DOUBLE, "
			+ "UNIQUE(name(255))"
			// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses MyISAM
			// too, we can't use InnoDB here
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS miner_info";
	protected static String SELECT_MINER_INFO_SQL = ""
			+ "SELECT id, name, shiatsu, miner, included_files, minimal_items, minimal_support "
			+ "FROM miner_info WHERE id=?";
	protected static String SELECT_MINER_INFO_BY_NAME_SQL = ""
			+ "SELECT id, name, shiatsu, miner, included_files, minimal_items, minimal_support "
			+ "FROM miner_info WHERE name=?";
	protected static String SELECT_MINER_INFOS_SQL = ""
			+ "SELECT id, name, shiatsu, miner, included_files, minimal_items, minimal_support "
			+ "FROM miner_info";
	protected static String CREATE_MINER_INFO_SQL = ""
			+ "INSERT INTO miner_info (id, name, shiatsu, miner, included_files, minimal_items, minimal_support) "
			+ "VALUES (?,?,?,?,?,?,?)";
	protected static String DELETE_MINER_INFO_SQL = ""
			+ "DELETE FROM miner_info WHERE id=?";
	protected static String UPDATE_MINER_INFO_SQL = ""
			+ "UPDATE miner_info SET name=?, shiatsu=?, miner=?, included_files=?, minimal_items=?, minimal_support=? "
			+ "WHERE id=?";

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
	public MinerInfo find(int id) throws DataAccessException {
		MinerInfo result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_INFO_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerInfo();
				result.setId(rs.getInt("id"));
				result.setName(rs.getString("name"));
				result.setShiatsu(rs.getBoolean("shiatsu"));
				result.setMiner(rs.getBoolean("miner"));
				result.setCodeFiles(rs.getString("included_files"));
				result.setMinimalItems(rs.getInt("minimal_items"));
				result.setMinimalSupport(rs.getDouble("minimal_support"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public MinerInfo find(String minerInfoName) throws DataAccessException {
		MinerInfo result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					SELECT_MINER_INFO_BY_NAME_SQL);
			ps.setString(1, minerInfoName);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerInfo();
				result.setId(rs.getInt("id"));
				result.setName(rs.getString("name"));
				result.setShiatsu(rs.getBoolean("shiatsu"));
				result.setMiner(rs.getBoolean("miner"));
				result.setCodeFiles(rs.getString("included_files"));
				result.setMinimalItems(rs.getInt("minimal_items"));
				result.setMinimalSupport(rs.getDouble("minimal_support"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public Collection<MinerInfo> findAll() throws DataAccessException {
		ArrayList<MinerInfo> result = new ArrayList<MinerInfo>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_INFOS_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				MinerInfo minerInfo = new MinerInfo();
				minerInfo.setId(rs.getInt("id"));
				minerInfo.setName(rs.getString("name"));
				minerInfo.setShiatsu(rs.getBoolean("shiatsu"));
				minerInfo.setMiner(rs.getBoolean("miner"));
				minerInfo.setCodeFiles(rs.getString("included_files"));
				minerInfo.setMinimalItems(rs.getInt("minimal_items"));
				minerInfo.setMinimalSupport(rs.getDouble("minimal_support"));
				result.add(minerInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public MinerInfo create(MinerInfo minerInfo) throws DataAccessException {
		MinerInfo result = minerInfo;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(CREATE_MINER_INFO_SQL,
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, minerInfo.getId());
			ps.setString(2, minerInfo.getName());
			ps.setBoolean(3, minerInfo.isShiatsu());
			ps.setBoolean(4, minerInfo.isMiner());
			ps.setString(5, minerInfo.getCodeFiles().toString());
			ps.setInt(6, minerInfo.getMinimalItems());
			ps.setDouble(7, minerInfo.getMinimalSupport());
			ps.execute();

			rs = ps.getGeneratedKeys();
			if (rs != null && rs.next()) {
				result.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
		return result;
	}

	@Override
	public void delete(MinerInfo minerInfo) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DELETE_MINER_INFO_SQL);
			ps.setInt(1, minerInfo.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public void update(MinerInfo minerInfo) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(UPDATE_MINER_INFO_SQL);
			ps.setString(1, minerInfo.getName());
			ps.setBoolean(2, minerInfo.isShiatsu());
			ps.setBoolean(3, minerInfo.isMiner());
			ps.setString(4, minerInfo.getCodeFiles().toString());
			ps.setInt(5, minerInfo.getMinimalItems());
			ps.setDouble(6, minerInfo.getMinimalSupport());
			ps.setInt(7, minerInfo.getId());
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
