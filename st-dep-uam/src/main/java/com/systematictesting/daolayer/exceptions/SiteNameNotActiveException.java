package com.systematictesting.daolayer.exceptions;

public class SiteNameNotActiveException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SiteNameNotActiveException(String message){
		super(message);
	}
	
	public SiteNameNotActiveException(Exception e){
		super(e);
	}
}
