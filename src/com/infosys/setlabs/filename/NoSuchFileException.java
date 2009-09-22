package com.infosys.setlabs.filename;

public class NoSuchFileException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoSuchFileException(String id) {
		super("Couldn't find file with ID '" + id + "'");
	}
}
