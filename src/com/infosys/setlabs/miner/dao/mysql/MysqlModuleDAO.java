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
import com.infosys.setlabs.miner.dao.ModuleDAO;
import com.infosys.setlabs.miner.domain.Module;

/**
 * MySQL Module DAO
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlModuleDAO extends JdbcDAO implements ModuleDAO {
	public static String tableName = "miner_modules";	
	
	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlModuleDAO(Connection conn) {
		super(conn);
	}

	protected String createTableSQL() {
		return String.format("CREATE TABLE %s ("
				+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
				+ "module_name MEDIUMTEXT NOT NULL, "
				+ "UNIQUE(module_name(255))"
				// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses
				// MyISAM too, we can't use InnoDB here
				+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8", tableName);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", tableName);
	}

	protected String selectSQL() {
		return String.format("SELECT id, module_name FROM %s WHERE id=?", tableName);
	}

	protected String selectByNameSQL() {
		return String.format("SELECT id, module_name "
				+ "FROM %s WHERE module_name=?", tableName);
	}

	protected String selectAllSQL() {
		return String.format("SELECT id, module_name FROM %s", tableName);
	}

	protected String createSQL() {
		return String.format("INSERT INTO %s (id, module_name) "
				+ "VALUES (?,?)", tableName);
	}

	protected String deleteSQL() {
		return String.format("DELETE FROM %s WHERE id=?", tableName);
	}

	protected String updateSQL() {
		return String.format("UPDATE miner_modules SET module_name=? "
				+ "WHERE id=?", tableName);
	}

	protected String countSQL() {
		return String.format("SELECT COUNT(id) AS count FROM %s", tableName);
	}

	@Override
	public Module find(int id) throws DataAccessException {
		Module result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectSQL());
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new Module(rs.getInt("id"));
				result.setModuleName(rs.getString("module_name"));
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
	public Module find(String moduleName) throws DataAccessException {
		Module result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectByNameSQL());
			ps.setString(1, moduleName);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new Module(rs.getInt("id"));
				result.setModuleName(rs.getString("module_name"));
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
	public Collection<Module> findAll() throws DataAccessException {
		ArrayList<Module> result = new ArrayList<Module>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(selectAllSQL());
			rs = ps.executeQuery();
			while (rs.next()) {
				Module module = new Module(rs.getInt("id"));
				module.setModuleName(rs.getString("module_name"));
				result.add(module);
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
	public Module create(Module module)
			throws DataAccessException {
		Module result = module;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = this.getConnection().prepareStatement(createSQL(),
					Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, module.getId());
			ps.setString(2, module.getModuleName());
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
	public void delete(Module module) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(deleteSQL());
			ps.setInt(1, module.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public void update(Module module) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(updateSQL());
			ps.setString(1, module.getModuleName());
			ps.setInt(2, module.getId());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

	@Override
	public int count() {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(countSQL());
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