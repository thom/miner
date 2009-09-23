package com.infosys.setlabs.fism.formatter;

import org.kohsuke.args4j.Option;

public class CommandLineValues {
	@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
	private String db;

	@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER", required = true)
	private String user;

	@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
	private String pw;

	@Option(name = "-r", aliases = {"revs", "revisions"}, usage = "print revisions in transaction file")
	private boolean revs = false;
	
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

	public boolean getRevs() {
		return revs;
	}
	
	public boolean getAllFiles() {
		return allFiles;
	}
}
