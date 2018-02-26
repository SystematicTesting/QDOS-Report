/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.exceptions;

public class AccountActivationLinkNotValidException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AccountActivationLinkNotValidException(String message){
		super(message);
	}
	
	public AccountActivationLinkNotValidException(Exception e){
		super(e);
	}
}
