package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.MinerFrequentItemSet;

public interface MinerFrequentItemSetDAO
		extends
			ReadObjectDAO<MinerFrequentItemSet>,
			CreateTablesDAO {
}
