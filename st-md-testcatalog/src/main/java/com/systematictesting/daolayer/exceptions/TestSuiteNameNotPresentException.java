package com.systematictesting.daolayer.exceptions;

public class TestSuiteNameNotPresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TestSuiteNameNotPresentException(String message) {
		super(message);
	}
}
