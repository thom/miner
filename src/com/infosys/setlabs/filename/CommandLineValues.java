package com.infosys.setlabs.filename;

import org.kohsuke.args4j.Option;

public class CommandLineValues {
	@Option(name = "-d", aliases = {"database", "db"}, usage = "name of the database to connect to", metaVar = "DB", required = true)
	private String db;

	@Option(name = "-u", aliases = {"user", "login"}, usage = "user name to log in to the database", metaVar = "USER", required = true)
	private String user;

	@Option(name = "-p", aliases = {"password", "pw"}, usage = "password used to log in to the database", metaVar = "PASSWORD")
	private String pw;

	@Option(name = "-i", aliases = {"id"}, usage = "ID of the file", required = true)
	private Integer id;

	@Option(name = "-n", aliases = {"name", "nameonly"}, usage = "get only the name and not the path of the file")
	private Boolean nameOnly = false;

	public String getDb() {
		return db;
	}

	public String getUser() {
		return user;
	}

	public String getPw() {
		return pw;
	}

	public Integer getId() {
		return id;
	}

	public boolean getNameOnly() {
		return nameOnly;
	}
}
