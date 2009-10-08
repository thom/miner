package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ObjectDAO;
import com.infosys.setlabs.miner.domain.MinerModule;

public interface MinerModuleDAO extends ObjectDAO<MinerModule>, CreateTablesDAO {
	public MinerModule find(String moduleName) throws DataAccessException;
}
