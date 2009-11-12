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
	public static String tableName = "miner_infos";

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
		// TODO: Add additional fields
		return String.format("CREATE TABLE %s ("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(255) NOT NULL DEFAULT 'default', "
				+ "shiatsu BOOLEAN, maximum_module_depth INT, "
				+ "minimum_modifications INT, miner BOOLEAN, "
				+ "minimum_items INT, minimum_support DOUBLE, "
				+ "has_randomized_modules BOOLEAN, UNIQUE(name(255))"
				// MyISAM doesn't support foreign keys, but as
				// CVSAnaly2 uses MyISAM too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8", tableName);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", tableName);
	}

	protected String selectSQL() {
		return String.format("SELECT id, name, shiatsu, maximum_module_depth, "
				+ "minimum_modifications, miner, minimum_items, "
				+ "minimum_support, has_randomized_modules "
				+ "FROM %s WHERE id=?", tableName);
	}

	protected String selectByNameSQL() {
		return String.format("SELECT id, name, shiatsu, maximum_module_depth, "
				+ "minimum_modifications, miner, minimum_items, "
				+ "minimum_support, has_randomized_modules "
				+ "FROM %s WHERE name=?", tableName);
	}

	protected String selectAllSQL() {
		return String.format("SELECT id, name, shiatsu, maximum_module_depth, "
				+ "minimum_modifications, miner, minimum_items, "
				+ "minimum_support, has_randomized_modules FROM %s", tableName);
	}

	protected String createSQL() {
		return String.format("INSERT INTO %s (id, name, shiatsu, "
				+ "maximum_module_depth, minimum_modifications, miner, "
				+ "minimum_items, minimum_support, has_randomized_modules) "
				+ "VALUES (?,?,?,?,?,?,?,?,?)", tableName);
	}

	protected String deleteSQL() {
		return String.format("DELETE FROM %s WHERE id=?", tableName);
	}

	protected String updateSQL() {
		return String.format("UPDATE %s SET name=?, shiatsu=?, "
				+ "maximum_module_depth=?, minimum_modifications=?, "
				+ "miner=?, minimum_items=?, minimum_support=?, "
				+ "has_randomized_modules=? WHERE id=?", tableName);
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
				result.setMaximumModuleDepth(rs.getInt("maximum_module_depth"));
				result.setMinimumModifications(rs
						.getInt("minimum_modifications"));
				result.setMiner(rs.getBoolean("miner"));
				result.setMinimumItems(rs.getInt("minimum_items"));
				result.setMinimumSupport(rs.getDouble("minimum_support"));
				result.setRandomizedModules(rs
						.getBoolean("has_randomized_modules"));
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
				result.setMaximumModuleDepth(rs.getInt("maximum_module_depth"));
				result.setMinimumModifications(rs
						.getInt("minimum_modifications"));
				result.setMiner(rs.getBoolean("miner"));
				result.setMinimumItems(rs.getInt("minimum_items"));
				result.setMinimumSupport(rs.getDouble("minimum_support"));
				result.setRandomizedModules(rs
						.getBoolean("has_randomized_modules"));
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
				minerInfo.setMaximumModuleDepth(rs
						.getInt("maximum_module_depth"));
				minerInfo.setMinimumModifications(rs
						.getInt("minimum_modifications"));
				minerInfo.setMiner(rs.getBoolean("miner"));
				minerInfo.setMinimumItems(rs.getInt("minimum_items"));
				minerInfo.setMinimumSupport(rs.getDouble("minimum_support"));
				minerInfo.setRandomizedModules(rs
						.getBoolean("has_randomized_modules"));
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
			ps.setInt(4, minerInfo.getMaximumModuleDepth());
			ps.setInt(5, minerInfo.getMinimumModifications());
			ps.setBoolean(6, minerInfo.isMiner());
			ps.setInt(7, minerInfo.getMinimumItems());
			ps.setDouble(8, minerInfo.getMinimumSupport());
			ps.setBoolean(9, minerInfo.hasRandomizedModules());
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
			ps.setInt(3, minerInfo.getMaximumModuleDepth());
			ps.setInt(4, minerInfo.getMinimumModifications());
			ps.setBoolean(5, minerInfo.isMiner());
			ps.setInt(6, minerInfo.getMinimumItems());
			ps.setDouble(7, minerInfo.getMinimumSupport());
			ps.setBoolean(8, minerInfo.hasRandomizedModules());
			ps.setInt(9, minerInfo.getId());
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
