package com.systematictesting.daolayer.exceptions;

public class UserSiteAlreadyPresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserSiteAlreadyPresentException(String message){
		super(message);
	}
	
	public UserSiteAlreadyPresentException(Exception e){
		super(e);
	}
}
