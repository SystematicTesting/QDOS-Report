/**
 * Copyright (c) Mar 9, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.daolayer.exceptions;

public class InternalServerErrorException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(String message){
		super(message);
	}
}
