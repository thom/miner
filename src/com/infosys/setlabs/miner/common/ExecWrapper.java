package com.infosys.setlabs.miner.common;

import java.io.File;
import java.io.IOException;

/**
 * Wraps executions of external commands
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ExecWrapper extends Thread {
	private boolean decorate;
	private String[] cmd;
	private String[] envp;
	private File dir;

	/**
	 * Creates a new execution wrapper
	 * 
	 * @param cmd
	 *            command to execute
	 */
	public ExecWrapper(String[] cmd) {
		this.cmd = cmd;
	}

	/**
	 * Sets decorate
	 * 
	 * @param decorate
	 *            should the output be decorated?
	 */
	public void setDecorate(boolean decorate) {
		this.decorate = decorate;
	}

	/**
	 * Sets envp
	 * 
	 * @param envp
	 *            environment to set
	 */
	public void setEnvp(String[] envp) {
		this.envp = envp;
	}

	/**
	 * Sets dir
	 * 
	 * @param dir
	 *            directory to set
	 */
	public void setDir(File dir) {
		this.dir = dir;
	}

	/**
	 * Executes the program
	 */
	public void run() {
		if (decorate) {
			System.out.print("EXEC  > ");
			for (String str : cmd) {
				System.out.print(str + " ");
			}
			System.out.println();
		}

		try {
			Process p = Runtime.getRuntime().exec(cmd, envp, dir);

			// Any error message?
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
					decorate ? "OUTPUT" : null);

			// Any output?
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(),
					decorate ? "OUTPUT" : null);

			// Kick them off
			errorGobbler.start();
			outputGobbler.start();

			// Any error?
			int exitVal = p.waitFor();
			if (decorate) {
				System.out
						.println("DONE  > " + cmd[0] + " (" + exitVal + ")\n");
			} else {
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
