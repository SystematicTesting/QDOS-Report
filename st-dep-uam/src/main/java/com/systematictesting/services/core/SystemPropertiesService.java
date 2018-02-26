package com.systematictesting.services.core;

import java.io.IOException;
import java.util.Properties;

public interface SystemPropertiesService {
	String SERVICE_NAME = "SystemPropertiesService";
	Properties getProperties() throws IOException;
}
