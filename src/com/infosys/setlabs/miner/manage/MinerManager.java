package com.infosys.setlabs.miner.manage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.infosys.setlabs.miner.common.ExecWrapper;
import com.infosys.setlabs.miner.common.MinerException;

/**
 * Miner Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
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
	 * @param minSize
	 * 
	 * @throws MinerException
	 */
	public void format(File transactions, boolean allFiles, boolean revs,
			int modifications, int minSize, int maxSize) throws MinerException {
		BasketFormatManager basketFormatManager = null;

		try {
			// Connect to MySQL database
			basketFormatManager = new BasketFormatManager(connectionArgs);

			// Format and write transactions to a file
			basketFormatManager.format(transactions, allFiles, revs,
					modifications, minSize, maxSize);
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
			int maxItems, File transactions, File frequentItemSets)
			throws MinerException {
		String[] cmd = getCmd(exec, minSupport, minItems, maxItems,
				transactions.getAbsolutePath(), frequentItemSets
						.getAbsolutePath());
		ExecWrapper apriori = new ExecWrapper(cmd, System.out, System.out);
		apriori.setDebug(true);
		apriori.run();
		apriori.checkReturnValue(0);
	}

	// Java arrays really suck!
	private String[] getCmd(String exec, double minSupport, int minItems,
			int maxItems, String transactions, String frequentItemSets) {
		if (maxItems > -1) {
			String[] result = { exec, "-tm", "-s" + minSupport,
					"-m" + minItems, "-n" + maxItems, "-v:%a %4S",
					transactions, frequentItemSets };
			return result;
		} else {
			String[] result = { exec, "-tm", "-s" + minSupport,
					"-m" + minItems, "-v:%a %4S", transactions,
					frequentItemSets };
			return result;
		}
	}

	/**
	 * Writes frequent item sets to the database
	 * 
	 * @throws MinerException
	 */
	public void frequentItemSets(File frequentItemSets, String name,
			boolean randomizedModules) throws MinerException {
		FrequentItemSetManager frequentItemSetManager = null;

		try {
			// Connect to MySQL database
			frequentItemSetManager = new FrequentItemSetManager(connectionArgs);
			frequentItemSetManager.setName(name);
			frequentItemSetManager.setRandomizedModules(randomizedModules);

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
