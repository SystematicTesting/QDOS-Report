package com.systematictesting.daolayer.exceptions;

public class MissingMandatoryAPIParameters extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MissingMandatoryAPIParameters(String message){
		super(message);
	}
}
