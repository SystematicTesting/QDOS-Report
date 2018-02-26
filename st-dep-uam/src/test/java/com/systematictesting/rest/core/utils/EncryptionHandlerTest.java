package com.systematictesting.rest.core.utils;

import org.junit.Test;

import com.systematictesting.rest.core.utils.EncryptionHandler;

import junit.framework.Assert;

public class EncryptionHandlerTest {

	@Test
	public void testEncrypt() {
		String keyToDecrypt = "testDb";
		String strToEncrypt = "testDb";
		System.out.println("ENCRYPTED VALUE : TEST 1 : "+EncryptionHandler.encrypt(strToEncrypt,keyToDecrypt));
		Assert.assertEquals("7hYQXnl+Tg7dFXMHY6iskw==", EncryptionHandler.encrypt(strToEncrypt,keyToDecrypt));
	}

	@Test
	public void testDecrypt() {
		String strToDecrypt = "/Gujie+MYDRrRxFjPB7pNQ";
		String keyToDecrypt = "krishna.kodavati@shell.com";
		System.out.println("DECRYPTED VALUE : TEST 2 : "+EncryptionHandler.decrypt(strToDecrypt,keyToDecrypt));
		Assert.assertEquals("password", EncryptionHandler.decrypt(strToDecrypt,keyToDecrypt));
	}

}
