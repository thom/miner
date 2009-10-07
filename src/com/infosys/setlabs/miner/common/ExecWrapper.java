package com.infosys.setlabs.miner.common;

import java.io.IOException;

public class ExecWrapper extends Thread {
	private String[] cmd;

	public ExecWrapper(String[] cmd) {
		this.cmd = cmd;
	}

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
			System.out.println("EXIT  > " + exitVal + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
