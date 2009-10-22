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
	/**
	 * Creates a new miner info DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerInfoDAO(Connection conn) {
		super(conn);
	}
	
	protected String createTableSQL() {
		return String.format("CREATE TABLE %s ("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(255) NOT NULL DEFAULT 'default', "
				+ "shiatsu BOOLEAN, miner BOOLEAN, "
				+ "included_files VARCHAR(255), minimal_items INT, "
				+ "minimal_support DOUBLE, UNIQUE(name(255))"
				// MyISAM doesn't support foreign keys, but as
				// CVSAnaly2 uses
				// MyISAM
				// too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8", name);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", name);
	}

	protected String selectSQL() {
		return String.format("SELECT id, name, shiatsu, miner, "
				+ "included_files, minimal_items, minimal_support "
				+ "FROM %s WHERE id=?", name);
	}

	protected String selectByNameSQL() {
		return String.format("SELECT id, name, shiatsu, miner, "
				+ "included_files, minimal_items, minimal_support "
				+ "FROM %s WHERE name=?", name);
	}

	protected String selectAllSQL() {
		return String.format("SELECT id, name, shiatsu, miner, "
				+ "included_files, " + "minimal_items, "
				+ "minimal_support FROM %s", name);
	}

	protected String createSQL() {
		return String.format("INSERT INTO %s (id, name, shiatsu, miner, "
				+ "included_files, minimal_items, minimal_support) "
				+ "VALUES (?,?,?,?,?,?,?)", name);
	}

	protected String deleteSQL() {
		return String.format("DELETE FROM %s WHERE id=?", name);
	}

	protected String updateSQL() {
		return String.format("UPDATE %s SET name=?, shiatsu=?, miner=?, "
				+ "included_files=?, minimal_items=?, minimal_support=? "
				+ "WHERE id=?", name);
	}	

	@Override
	public MinerInfo find(int id) throws DataAccessException {
		MinerInfo result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
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
			ps = this.getConnection().prepareStatement(selectByNameSQL());
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
			ps = this.getConnection().prepareStatement(selectAllSQL());
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
			ps = this.getConnection().prepareStatement(createSQL(),
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
			ps = this.getConnection().prepareStatement(deleteSQL());
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
			ps = this.getConnection().prepareStatement(updateSQL());
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
			ps = this.getConnection().prepareStatement(dropTableSQL());
			ps.executeUpdate();
			ps.executeUpdate(createTableSQL());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}
