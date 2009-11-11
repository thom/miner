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
	 * Call "git branch -a" and "git tag" in the repository directory
	 * 
	 * @param repository
	 *            repository directory
	 */
	public void showBranchesAndTags(String repository) throws MinerException {
		String[] branchCmd = {"git", "branch", "-a"};
		ExecWrapper gitBranch = new ExecWrapper(branchCmd, System.out,
				System.out);
		gitBranch.setDir(new File(repository));
		System.out.println("Branches:\n");
		gitBranch.run();

		String[] tagsCmd = {"git", "tag"};
		ExecWrapper gitTags = new ExecWrapper(tagsCmd, System.out, System.out);
		gitTags.setDir(new File(repository));
		System.out.println("\nTags:\n");
		gitTags.run();
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
		String[] logCmd = getCmd(branch, all);
		String[] replaceCmd = {"sed", "-i",
				"1s@tag: refs/tags/@refs/remotes/origin/@",
				log.getAbsolutePath()};
		try {
			ExecWrapper git = new ExecWrapper(logCmd, new PrintStream(log),
					System.out);
			git.setDir(new File(repository));
			git.setDebug(true);
			git.run();
			if (git.getExitVal() != 0) {
				String error = "Error while executing ";
				for (String str : logCmd) {
					error += str + " ";
				}
				throw new MinerException(new Exception(error));
			}

			ExecWrapper sed = new ExecWrapper(replaceCmd, System.out,
					System.out);
			sed.setDebug(true);
			sed.run();
			if (sed.getExitVal() != 0) {
				String error = "Error while executing ";
				for (String str : replaceCmd) {
					error += str + " ";
				}
				throw new MinerException(new Exception(error));
			}
		} catch (FileNotFoundException e) {
			throw new MinerException(e);
		}
	}
	// Java arrays really suck!
	private String[] getCmd(String branch, boolean all) {
		if (all) {
			String[] result = {"git", "log", "--all", "--topo-order",
					"--pretty=fuller", "--parents", "--name-status", "-M",
					"-C", "--decorate", branch};
			return result;
		} else {
			String[] result = {"git", "log", "--topo-order", "--pretty=fuller",
					"--parents", "--name-status", "-M", "-C", "--decorate",
					branch};
			return result;
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
			String error = "Error while executing ";
			for (String str : cmd) {
				error += str + " ";
			}
			throw new MinerException(new Exception(error));
		}
	}
}