package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ObjectDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

public interface MinerInfoDAO extends ObjectDAO<MinerInfo>, CreateTablesDAO {
	/**
	 * Name
	 */
	public static String name = "miner_infos";
	
	/**
	 * Finds a miner info by name.
	 * 
	 * @param minerInfoName
	 *            ID to find
	 * @return miner info
	 * @throws DataAccessException
	 */
	public MinerInfo find(String minerInfoName) throws DataAccessException;
}
