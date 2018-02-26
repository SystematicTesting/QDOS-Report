package com.systematictesting.daolayer.exceptions;

public class TestSuiteNameAlreadyPresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TestSuiteNameAlreadyPresentException(String message) {
		super(message);
	}
}
