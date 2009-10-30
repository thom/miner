package com.infosys.setlabs.miner.manage;

import java.io.File;
import java.util.HashMap;

import com.infosys.setlabs.miner.common.ExecWrapper;
import com.infosys.setlabs.miner.common.MinerException;

/**
 * Gitup Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class GitupManager extends Manager {
	private HashMap<String, String> connectionArgs = null;

	/**
	 * Creates a new gitup mananger
	 * 
	 * @param connectionArgs
	 *            arguments to use for connection
	 * @throws MinerException
	 */
	public GitupManager(HashMap<String, String> connectionArgs)
			throws MinerException {
		super(connectionArgs);
		this.connectionArgs = connectionArgs;
	}

	/**
	 * Call "git branch -a" in the repository directory
	 * 
	 * @param repository
	 *            repository directory
	 */
	public void showBranches(String repository) {
		String[] cmd = {"git", "branch", "-a"};
		ExecWrapper git = new ExecWrapper(cmd);
		git.setDir(new File(repository));
		git.run();
	}
}
