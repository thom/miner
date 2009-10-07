package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

public interface FrequentItemSetDAO
		extends
			ReadObjectDAO<FrequentItemSet>,
			CreateTablesDAO {
}
