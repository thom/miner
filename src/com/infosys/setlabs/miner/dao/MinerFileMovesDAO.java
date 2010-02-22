package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;

public interface MinerFileMovesDAO extends CreateTablesDAO {
	/**
	 * Initializes the miner file moves table
	 * 
	 * @throws DataAccessException
	 */
	public void initialize() throws DataAccessException;
}
