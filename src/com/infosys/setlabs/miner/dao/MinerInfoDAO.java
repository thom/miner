package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.UpdateObjectDAO;
import com.infosys.setlabs.miner.domain.MinerInfo;

public interface MinerInfoDAO extends UpdateObjectDAO<MinerInfo>, CreateTablesDAO {
	/**
	 * Returns the miner info
	 * 
	 * @return MinerInfo
	 */
	public MinerInfo get();
}
