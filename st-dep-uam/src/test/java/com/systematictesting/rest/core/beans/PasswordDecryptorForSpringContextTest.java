package com.systematictesting.rest.core.beans;

import org.junit.Test;

import com.systematictesting.rest.core.beans.PasswordDecryptorForSpringContext;
import com.systematictesting.rest.core.utils.EncryptionHandler;

import junit.framework.Assert;

public class PasswordDecryptorForSpringContextTest {

	private static String ENCRYPTED_PROPERTY_POSTFIX = ".password.encrypted";
	
	@Test
	public void testConvertPropertyStringString() {
		PasswordDecryptorForSpringContext objPasswordDecryptorForSpringContext = new PasswordDecryptorForSpringContext();
		String password = "testST";
		String encodedPassword = EncryptionHandler.encode(password);
		String decodedPassword = objPasswordDecryptorForSpringContext.convertProperty(password+ENCRYPTED_PROPERTY_POSTFIX, encodedPassword);
		System.out.println("ENCODED PASSWORD : "+encodedPassword);
		Assert.assertEquals(password,decodedPassword);
	}

}
