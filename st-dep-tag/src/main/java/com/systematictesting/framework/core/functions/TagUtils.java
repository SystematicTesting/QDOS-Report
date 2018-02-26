package com.systematictesting.framework.core.functions;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagUtils {

	private static final Logger logger = LoggerFactory.getLogger(TagUtils.class);
	
	public static boolean isRequestedFeatureAllowed(String featureName, Map featureList) {
		logger.debug("@@@ Checking Feature availability = "+featureName);
		boolean result = false;
		logger.debug("@@@ Feature List = "+featureList);
		if (featureList!=null && featureList.size()>0){
			if (featureList.containsKey(featureName.trim())){
				Long expiryTime = (Long)featureList.get(featureName.trim());
				Calendar expiryDate = Calendar.getInstance();    
				expiryDate.setTime(new Date(expiryTime));
				expiryDate.add(Calendar.DATE, 1);
				Long todayTime = System.currentTimeMillis();
				if (expiryDate.getTimeInMillis()>=todayTime){
					return true;
				} else {
					return false;
				}
			} else {
				result = false;
			}
		}
		return result;
	}
	
}
