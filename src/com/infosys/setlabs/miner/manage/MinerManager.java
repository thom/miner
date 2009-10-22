package com.infosys.setlabs.miner.manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.infosys.setlabs.miner.common.ExecWrapper;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.BasketFormatDAO.CodeFiles;

public class MinerManager extends Manager {
	private HashMap<String, String> connectionArgs = null;

	/**
	 * Creates a new miner mananger
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public MinerManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
		this.connectionArgs = connectionArgs;
	}

	/**
	 * Formats revision history into basket format
	 * 
	 * @throws MinerException
	 */
	public void format(File transactions, CodeFiles codeFiles, boolean revs)
			throws MinerException {
		BasketFormatManager basketFormatManager = null;

		try {
			// Connect to MySQL database
			basketFormatManager = new BasketFormatManager(connectionArgs);

			// Format and write transactions to a file
			basketFormatManager.format(transactions, codeFiles, revs);
		} finally {
			if (basketFormatManager != null) {
				basketFormatManager.close();
			}
		}
	}

	/**
	 * Calls <code>apriori</code> frequent item set miner
	 * 
	 * @throws MinerException
	 */
	public void apriori(String exec, double minSupport, int minItems,
			File transactions, File frequentItemSets) throws MinerException {
		String[] cmd = {exec, "-s" + minSupport, "-m" + minItems, "-v:%a %4S",
				transactions.getAbsolutePath(),
				frequentItemSets.getAbsolutePath()};
		ExecWrapper apriori = new ExecWrapper(cmd);
		apriori.run();
	}

	/**
	 * Writes frequent item sets to the database
	 * 
	 * @throws MinerException
	 */
	public void frequentItemSets(File frequentItemSets, String name) throws MinerException {
		FrequentItemSetManager frequentItemSetManager = null;

		try {
			// Connect to MySQL database
			frequentItemSetManager = new FrequentItemSetManager(connectionArgs);
			frequentItemSetManager.setName(name);

			// Create tables
			frequentItemSetManager.createTables();

			// Iterate over output of apriori and add frequent item sets
			// to the database
			BufferedReader in = new BufferedReader(new FileReader(
					frequentItemSets));
			String line;
			while ((line = in.readLine()) != null) {
				frequentItemSetManager.create(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (frequentItemSetManager != null) {
				frequentItemSetManager.close();
			}
		}
	}
}
