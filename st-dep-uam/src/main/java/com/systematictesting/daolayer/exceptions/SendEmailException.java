/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.exceptions;

public class SendEmailException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SendEmailException(String message){
		super(message);
	}
	
	public SendEmailException(Exception e){
		super(e);
	}
}
