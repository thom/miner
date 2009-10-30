package com.infosys.setlabs.miner.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Gobbles streams
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class StreamGobbler extends Thread {
	InputStream in;
	PrintStream out;

	/**
	 * Creates a new stream gobbler
	 * 
	 * @param in
	 *            input stream
	 * @param out
	 *            output stream
	 */
	StreamGobbler(InputStream in, PrintStream out) {
		this.in = in;
		this.out = out;
	}

	/**
	 * Runs the stream gobbler
	 */
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null) {
				out.println(line);
			}

			isr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
