package com.infosys.setlabs.miner.manage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
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
	public void showBranches(String repository) throws MinerException {
		String[] cmd = {"git", "branch", "-a"};
		ExecWrapper git = new ExecWrapper(cmd, System.out, System.out);
		git.setDir(new File(repository));
		git.run();
	}

	/**
	 * TODO
	 * 
	 * @param repository
	 * @param branch
	 * @param log
	 */
	public void generateLog(String repository, String branch, File log)
			throws MinerException {
		String[] cmd = {"git", "log", "--topo-order", "--pretty=fuller",
				"--parents", "--name-status", "-M", "-C", "--decorate", branch};
		try {
			ExecWrapper git = new ExecWrapper(cmd, new PrintStream(log),
					System.out);
			git.setDir(new File(repository));
			git.setDebug(true);			
			git.run();
			if (git.getExitVal() != 0) {
				System.out.print("\nError while executing ");
				for (String str : cmd) {
					System.out.print(str + " ");
				}
				System.out.println("\n");
			}
		} catch (FileNotFoundException e) {
			throw new MinerException(e);
		}
	}
}
