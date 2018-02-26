/**
 * Copyright (c) Mar 8, 2015 Sharad Tech Ltd. (www.sharadtech.com) to Present.
 * All rights reserved. 
 */
package com.systematictesting.rest.core.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.thoughtworks.xstream.core.util.Base64Encoder;

public class EncryptionHandler {

	public static String getMD5String(String data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(data.getBytes("UTF-8"));
			byte byteData[] = md.digest();
			StringBuilder hexString = new StringBuilder();
			for (byte singleByte : byteData) {
				String hex = Integer.toHexString(0xff & singleByte);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "NULL";
	}

	public static String encrypt(String strToEncrypt, String keyToEncrypt) {
		try {
			String md5KeyString = EncryptionHandler.getMD5String(keyToEncrypt);
			byte[] key = md5KeyString.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key,0,16,"AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			final String encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
			return encryptedString;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static String decrypt(String strToDecrypt, String keyToDecrypt) {
		try {
			String md5KeyString = EncryptionHandler.getMD5String(keyToDecrypt);
			byte[] key = md5KeyString.getBytes("UTF-8");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			final SecretKeySpec secretKey = new SecretKeySpec(key,0,16,"AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
			return decryptedString;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}
	
	public static String encode(String str) {
        Base64Encoder encoder = new Base64Encoder();
        str = new String(encoder.encode(str.getBytes()));
        return str;
    }
	
	public static String decode(String password) {
        Base64Encoder decoder = new Base64Encoder();
        String decodedPassword = null;
        decodedPassword = new String(decoder.decode(password));       
        return decodedPassword;
    }

}
