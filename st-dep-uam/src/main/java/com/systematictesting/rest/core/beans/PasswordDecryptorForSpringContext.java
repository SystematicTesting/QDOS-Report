package com.systematictesting.rest.core.beans;

import com.systematictesting.rest.core.utils.EncryptionHandler;

public class PasswordDecryptorForSpringContext extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	private static String ENCRYPTED_PROPERTY_POSTFIX = ".password.encrypted";

	protected String convertProperty(String propertyName, String propertyValue) {
		String result = super.convertProperty(propertyName, propertyValue);
		if (propertyName!=null && propertyName.endsWith(ENCRYPTED_PROPERTY_POSTFIX)){
			result = EncryptionHandler.decode(propertyValue);
		}
		return result;
	}
}
