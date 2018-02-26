/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.exceptions;

public class SessionNotValidException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SessionNotValidException(String message){
		super(message);
	}
	
	public SessionNotValidException(Exception e){
		super(e);
	}
}
