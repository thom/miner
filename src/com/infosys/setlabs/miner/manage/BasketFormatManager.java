package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;

/**
 * Basket Format Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class BasketFormatManager extends Manager {
	/**
	 * Creates a new basket format manager
	 * 
	 * @throws MinerException
	 */
	public BasketFormatManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
	}

	/**
	 * Formats transactions to basket format
	 * 
	 * @param allFiles
	 *            should all files be added to transactions?
	 * @param revs
	 *            should revisions be written in comments?
	 * @return transactions in basket format
	 * @throws MinerException
	 */
	public String format(boolean allFiles, boolean revs) throws MinerException {
		DAOTransaction trans = null;
		String result = "";
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			result = this.getFactory().getBasketFormatDAO(this.getSession())
					.format(allFiles, revs);

			// Commit transaction
			trans.commit();
		} catch (DataAccessException de) {
			// Rollback transaction on failure
			try {
				if (trans != null)
					trans.abort();
			} catch (DataAccessException de2) {
				throw new MinerException(de2);
			}
			throw new MinerException(de);
		}
		return result;
	}
}
