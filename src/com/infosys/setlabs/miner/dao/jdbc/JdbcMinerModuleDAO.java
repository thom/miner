package com.infosys.setlabs.miner.dao.jdbc;

import java.sql.Connection;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.domain.MinerModule;

public class JdbcMinerModuleDAO extends JdbcDAO implements MinerModuleDAO {

	// TODO: SQL strings
	
	public JdbcMinerModuleDAO(Connection conn) {
		super(conn);
	}

	@Override
	public MinerModule find(int id) throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public Collection<MinerModule> findAll() throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public void create(MinerModule object) throws DataAccessException {
		// TODO
	}

	@Override
	public void delete(MinerModule object) throws DataAccessException {
		// TODO
	}

	@Override
	public void update(MinerModule object) throws DataAccessException {
		// TODO
	}
}