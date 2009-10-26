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
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.domain.MinerModule;

/**
 * MySQL Miner Module DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlMinerModuleDAO extends JdbcDAO implements MinerModuleDAO {
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerModuleDAO(Connection conn) {
		super(conn);
	}

	protected String createTableSQL() {
		return String.format("CREATE TABLE %s ("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "module_name MEDIUMTEXT NOT NULL, "
				+ "has_renamed_files BOOLEAN, " + "UNIQUE(module_name(255))"
				// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses
				// MyISAM too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8", name);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", name);
	}

	protected String selectSQL() {
		return String.format("SELECT id, module_name, has_renamed_files "
				+ "FROM %s WHERE id=?", name);
	}

	protected String selectByNameSQL() {
		return String.format("SELECT id, module_name, has_renamed_files "
				+ "FROM %s WHERE module_name=?", name);
	}

	protected String selectAllSQL() {
		return String.format("SELECT id, module_name, has_renamed_files "
				+ "FROM %s", name);
	}

	protected String createSQL() {
		return String.format(
				"INSERT INTO %s (id, module_name, has_renamed_files) "
						+ "VALUES (?,?,?)", name);
	}

	protected String deleteSQL() {
		return String.format("DELETE FROM %s WHERE id=?", name);
	}

	protected String updateSQL() {
		return String.format("UPDATE miner_modules "
				+ "SET module_name=?, has_renamed_files=? " + "WHERE id=?",
				name);
	}

	protected String countSQL(boolean allModules) {
		if (allModules) {
			return String.format("SELECT COUNT(id) FROM %s", name);			
		} else {
			return String.format(
					"SELECT COUNT(id) FROM %s WHERE has_renamed_files", name);			
		}
	}

	@Override
	public MinerModule find(int id) throws DataAccessException {
		MinerModule result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerModule(rs.getInt("id"));
				result.setModuleName(rs.getString("module_name"));
				result.setRenamedFiles(rs.getBoolean("has_renamed_files"));
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
	public MinerModule find(String moduleName) throws DataAccessException {
		MinerModule result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectByNameSQL());
			ps.setString(1, moduleName);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerModule(rs.getInt("id"));
				result.setModuleName(rs.getString("module_name"));
				result.setRenamedFiles(rs.getBoolean("has_renamed_files"));
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
	public Collection<MinerModule> findAll() throws DataAccessException {
		ArrayList<MinerModule> result = new ArrayList<MinerModule>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectAllSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				MinerModule minerModule = new MinerModule(rs.getInt("id"));
				minerModule.setModuleName(rs.getString("module_name"));
				minerModule.setRenamedFiles(rs.getBoolean("has_renamed_files"));
				result.add(minerModule);
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
	public MinerModule create(MinerModule minerModule)
			throws DataAccessException {
		MinerModule result = minerModule;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(createSQL(),
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, minerModule.getId());
			ps.setString(2, minerModule.getModuleName());
			ps.setBoolean(3, minerModule.hasRenamedFiles());
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
	public void delete(MinerModule minerModule) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(deleteSQL());
			ps.setInt(1, minerModule.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public void update(MinerModule minerModule) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(updateSQL());
			ps.setString(1, minerModule.getModuleName());
			ps.setBoolean(2, minerModule.hasRenamedFiles());
			ps.setInt(3, minerModule.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public int count(boolean allModules) {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					countSQL(allModules));
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt("count");
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