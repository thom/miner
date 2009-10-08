package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.CreateObjectDAO;
import com.infosys.setlabs.dao.CreateTablesDAO;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.ReadObjectDAO;
import com.infosys.setlabs.miner.domain.FrequentItemSet;

public interface FrequentItemSetDAO
		extends
			ReadObjectDAO<FrequentItemSet>,
			CreateObjectDAO<FrequentItemSet>,
			CreateTablesDAO {
	/**
	 * Expects an input in the form of <code>5 23 42:10 23.4200</code> whereas
	 * before the <code>:</code> are file IDs and after the <code>:</code> are
	 * the absolute and relative item support.
	 * 
	 * @param frequentItemSetLine
	 * @return ID of newly created frequent item set
	 * @throws DataAccessException
	 */
	public int create(String frequentItemSetLine) throws DataAccessException;
}
