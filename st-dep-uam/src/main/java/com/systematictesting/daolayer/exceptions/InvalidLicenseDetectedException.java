package com.systematictesting.daolayer.exceptions;

public class InvalidLicenseDetectedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidLicenseDetectedException(String message){
		super(message);
	}
}