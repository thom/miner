package com.infosys.setlabs.formatter;

import org.kohsuke.args4j.Option;

public class CommandLineValues {
	@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
	private String db;

	@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER", required = true)
	private String user;

	@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
	private String pw;

	@Option(name = "-np", aliases = {"no-password", "nopw"}, usage = "don't use a password to connect to the database")
	private boolean nopw = false;

	@Option(name = "-i", aliases = {"ids", "commitids"}, usage = "print commit ids in transaction file")
	private boolean ids = false;
	
	@Option(name = "-a", aliases = {"all", "all-files"}, usage="print all files affect by a transaction, including non-code files")
	private boolean allFiles = false;

	public String getDb() {
		return db;
	}

	public String getUser() {
		return user;
	}

	public String getPw() {
		return pw;
	}

	public boolean getNopw() {
		return nopw;
	}

	public boolean getIds() {
		return ids;
	}
	
	public boolean getAllFiles() {
		return allFiles;
	}
}
