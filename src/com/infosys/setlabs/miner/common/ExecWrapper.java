package com.infosys.setlabs.miner.common;

import java.io.IOException;

/**
 * Wraps executions of external commands
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class ExecWrapper extends Thread {
	private String[] cmd;

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
	 * Executes the program
	 */
	public void run() {
		System.out.print("EXEC  > ");
		for (String str : cmd) {
			System.out.print(str + " ");
		}
		System.out.println();

		try {
			Process p = Runtime.getRuntime().exec(cmd);

			// Any error message?
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(),
					"OUTPUT");

			// Any output?
			StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream(),
					"OUTPUT");

			// Kick them off
			errorGobbler.start();
			outputGobbler.start();

			// Any error?
			int exitVal = p.waitFor();
			System.out.println("DONE  > " + cmd[0] + " (" + exitVal + ")\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
