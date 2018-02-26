package com.systematictesting.daolayer.exceptions;

public class InvalidUploadPostRequestException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidUploadPostRequestException(String message){
		super(message);
	}
}