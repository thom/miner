package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateObjectDAO;
import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

public interface MinerInfoDAO extends CreateObjectDAO<MinerInfo>, CreateTablesDAO {
	/**
	 * Returns the miner info
	 * 
	 * @return MinerInfo
	 */
	public MinerInfo get();
}
