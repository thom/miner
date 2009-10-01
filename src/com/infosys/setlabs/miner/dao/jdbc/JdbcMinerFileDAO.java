package com.infosys.setlabs.miner.dao.jdbc;

import java.sql.Connection;
import java.util.Collection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

public class JdbcMinerFileDAO extends JdbcDAO implements MinerFileDAO {

	// TODO: SQL strings	
	
	public JdbcMinerFileDAO(Connection conn) {
		super(conn);
	}

	@Override
	public MinerFile find(int id) throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public Collection<MinerFile> findAll() throws DataAccessException {
		// TODO
		return null;
	}

	@Override
	public void create(MinerFile object) throws DataAccessException {
		// TODO
	}

	@Override
	public void delete(MinerFile object) throws DataAccessException {
		// TODO
	}

	@Override
	public void update(MinerFile object) throws DataAccessException {
		// TODO
	}
}
