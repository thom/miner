package com.infosys.setlabs.miner.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.domain.MinerModule;

public class JdbcMinerModuleDAO extends JdbcDAO implements MinerModuleDAO {

	protected static String SELECT_MINER_MODULE_SQL = ""
			+ "SELECT id, module_name FROM \"minder_modules\" WHERE id=?";
	protected static String SELECT_MINER_MODULES_SQL = ""
			+ "SELECT id, module_name FROM \"miner_modules\"";
	protected static String CREATE_MINER_MODULE_SQL = ""
			+ "INSERT INTO miner_modules (module_name) VALUES (?)";
	protected static String DELETE_MINER_MODULE_SQL = ""
			+ "DELETE FROM \"miner_modules\" WHERE id=?";
	protected static String UPDATE_MINER_MODULE_SQL = ""
			+ "UPDATE \"miner_modules\" SET module_name=? WHERE id=?)";

	public JdbcMinerModuleDAO(Connection conn) {
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
	public void create(MinerModule minerModule) throws DataAccessException {
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(CREATE_MINER_MODULE_SQL);
			ps.setString(1, minerModule.getModuleName());
			ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
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
}