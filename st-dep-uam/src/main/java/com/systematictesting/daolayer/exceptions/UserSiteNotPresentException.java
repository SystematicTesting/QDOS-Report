package com.systematictesting.daolayer.exceptions;

public class UserSiteNotPresentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserSiteNotPresentException(String message){
		super(message);
	}
	
	public UserSiteNotPresentException(Exception e){
		super(e);
	}
}
