package com.systematictesting.daolayer.exceptions;

import java.io.IOException;

public class FileNotFoundException extends IOException {

	private static final long serialVersionUID = 1L;

	public FileNotFoundException(String message) {
		super(message);
	}
}
