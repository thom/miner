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

	protected static String CREATE_MINER_MODULES_TABLE = ""
			+ "CREATE TABLE miner_modules ("
			+ "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
			+ "module_name MEDIUMTEXT NOT NULL," + "UNIQUE(module_name(255))"
			// MyISAM doesn't support foreign keys, but as CVSAnaly2 uses MyISAM
			// too, we can't use InnoDB here
			+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8";
	protected static String DROP_MINER_MODULES_TABLE = ""
			+ "DROP TABLE IF EXISTS miner_modules";
	protected static String SELECT_MINER_MODULE_SQL = ""
			+ "SELECT id, module_name FROM miner_modules WHERE id=?";
	protected static String SELECT_MINER_MODULE_BY_NAME_SQL = ""
			+ "SELECT id, module_name FROM miner_modules "
			+ "WHERE module_name like ?";
	protected static String SELECT_MINER_MODULES_SQL = ""
			+ "SELECT id, module_name FROM miner_modules";
	protected static String CREATE_MINER_MODULE_SQL = ""
			+ "INSERT INTO miner_modules (id, module_name) VALUES (?,?)";
	protected static String DELETE_MINER_MODULE_SQL = ""
			+ "DELETE FROM miner_modules WHERE id=?";
	protected static String UPDATE_MINER_MODULE_SQL = ""
			+ "UPDATE miner_modules SET module_name=? WHERE id=?)";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */	
	public MysqlMinerModuleDAO(Connection conn) {
		super(conn);
	}

	@Override
	public MinerModule find(int id) throws DataAccessException {
		MinerModule result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(SELECT_MINER_MODULE_SQL);
			ps.setInt(1, id);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerModule(rs.getInt("id"));
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
	public MinerModule find(String moduleName) throws DataAccessException {
		MinerModule result = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection().prepareStatement(
					SELECT_MINER_MODULE_BY_NAME_SQL);
			ps.setString(1, moduleName);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = new MinerModule(rs.getInt("id"));
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
	public Collection<MinerModule> findAll() throws DataAccessException {
		ArrayList<MinerModule> result = new ArrayList<MinerModule>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.getConnection()
					.prepareStatement(SELECT_MINER_MODULES_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				MinerModule minerModule = new MinerModule(rs.getInt("id"));
				minerModule.setModuleName(rs.getString("module_name"));
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
	public int create(MinerModule minerModule) throws DataAccessException {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		MinerModule minerModuleDB = find(minerModule.getModuleName());

		// Only add miner module if it doesn't already exist in the database.
		// Else return the ID of the existing database entry.
		if (minerModuleDB != null)
			result = minerModuleDB.getId();
		else {
			try {
				ps = this.getConnection().prepareStatement(
						CREATE_MINER_MODULE_SQL,
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, minerModule.getId());
				ps.setString(2, minerModule.getModuleName());
				ps.execute();

				rs = ps.getGeneratedKeys();
				if (rs != null && rs.next())
					result = rs.getInt(1);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				this.closeStatement(ps);
			}
		}
		return result;
	}

	@Override
	public void delete(MinerModule minerModule) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(DELETE_MINER_MODULE_SQL);
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
			ps = this.getConnection().prepareStatement(UPDATE_MINER_MODULE_SQL);
			ps.setString(1, minerModule.getModuleName());
			ps.setInt(2, minerModule.getId());
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
			ps = this.getConnection()
					.prepareStatement(DROP_MINER_MODULES_TABLE);
			ps.executeUpdate();
			ps.executeUpdate(CREATE_MINER_MODULES_TABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}
}