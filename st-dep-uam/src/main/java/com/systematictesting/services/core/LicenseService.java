package com.systematictesting.services.core;

import java.util.Map;

public interface LicenseService {
	
	String SERVICE_NAME = "LicenseService";
	Map<String,Long> getAvailableFeatures(String email);
}
