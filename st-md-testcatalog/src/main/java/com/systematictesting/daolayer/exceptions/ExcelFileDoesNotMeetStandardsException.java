package com.systematictesting.daolayer.exceptions;

public class ExcelFileDoesNotMeetStandardsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExcelFileDoesNotMeetStandardsException(String message) {
		super(message);
	}
}
