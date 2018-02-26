package com.systematictesting.daolayer.exceptions;

public class InvalidExcelFileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidExcelFileException(String message) {
		super(message);
	}
}
