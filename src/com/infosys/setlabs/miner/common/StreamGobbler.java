package com.infosys.setlabs.miner.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Gobbles streams
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class StreamGobbler extends Thread {
	InputStream is;
	String type;

	/**
	 * Creates a new stream gobbler
	 * 
	 * @param is
	 *            input stream
	 * @param type
	 *            type of input stream
	 */
	StreamGobbler(InputStream is, String type) {
		this.is = is;
		this.type = type;
	}

	/**
	 * Runs the stream gobbler
	 */
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;

			while ((line = br.readLine()) != null) {
				System.out.println(type + "> " + line);
			}

			isr.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
