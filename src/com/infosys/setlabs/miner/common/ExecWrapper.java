package com.infosys.setlabs.miner.common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Wraps executions of external commands
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ExecWrapper {
	private String[] cmd;
	private String[] envp;
	private File dir;
	private int exitVal;
	private PrintStream stdOut;
	private PrintStream errOut;
	private boolean debug;

	/**
	 * Creates a new execution wrapper
	 * 
	 * @param cmd
	 *            command to execute
	 */
	public ExecWrapper(String[] cmd, PrintStream stdOut, PrintStream errOut) {
		this.cmd = cmd;
		this.stdOut = stdOut;
		this.errOut = errOut;
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
	 * Sets debug
	 * 
	 * @param debug
	 *            do you want to print debug output?
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Returns the command as a string
	 * 
	 * @return command
	 */
	public String getCmd() {
		String result = "";
		for (String str : cmd) {
			result += str + " ";
		}
		return result;
	}

	/**
	 * Returns the exit value
	 * 
	 * @return exitVal
	 */
	public int getExitVal() {
		return exitVal;
	}

	/**
	 * Executes the program
	 */
	public void run() throws MinerException {
		try {
			if (debug) {
				System.out.println("Running " + getCmd());
			}

			Process p = Runtime.getRuntime().exec(cmd, envp, dir);

			// Any error message?
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
					errOut);

			// Any output?
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(),
					stdOut);

			// Kick them off
			errorGobbler.start();
			outputGobbler.start();

			// Any error?
			this.exitVal = p.waitFor();
		} catch (IOException e) {
			throw new MinerException(e);
		} catch (InterruptedException e) {
			throw new MinerException(e);
		}
	}

	public void checkReturnValue(int value) throws MinerException {
		if (this.exitVal != value) {
			String errorMsg = "Error while executing ";
			for (String str : cmd) {
				errorMsg += str + " ";
			}
			throw new MinerException(new Exception(errorMsg));
		}
	}
}
