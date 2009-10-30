package com.infosys.setlabs.miner.manage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;

import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.ExecWrapper;
import com.infosys.setlabs.miner.common.MinerException;

/**
 * Gitup Manager
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class GitupManager extends Manager {
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
	 * Generates git commit log
	 * 
	 * @param repository
	 *            git repository
	 * @param branch
	 *            branch
	 * @param log
	 *            log file
	 */
	public void generateLog(String repository, String branch, File log,
			boolean all) throws MinerException {
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

	/**
	 * Creates database
	 * 
	 * @param name
	 *            database name
	 * @throws MinerException
	 */
	public void createDatabase(String name) throws MinerException {
		DAOTransaction trans = null;
		try {
			// Start new transaction
			trans = this.getSession().getTransaction();
			trans.begin();

			this.getFactory().getGitupDAO(this.getSession()).createDatabase(
					name);

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
	}

	/**
	 * Run cvsanaly2
	 * 
	 * @param exec
	 *            cvsanaly executable
	 * @param db
	 *            database name
	 * @param log
	 *            log file
	 * @param config
	 *            configuration file
	 * @throws MinerException
	 */
	public void cvsanaly(String exec, String db, File log, String config)
			throws MinerException {
		String[] cmd = {exec, "-d", db, "-l", log.getAbsolutePath(), "-f",
				config};
		ExecWrapper cvsanaly = new ExecWrapper(cmd, System.out, System.out);
		cvsanaly.setDebug(true);
		cvsanaly.run();
		if (cvsanaly.getExitVal() != 0) {
			System.out.print("\nError while executing ");
			for (String str : cmd) {
				System.out.print(str + " ");
			}
			System.out.println("\n");
		}

	}
}