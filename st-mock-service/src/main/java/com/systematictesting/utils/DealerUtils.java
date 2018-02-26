package com.systematictesting.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class DealerUtils {
	private static final Logger logger = LoggerFactory.getLogger(DealerUtils.class);
	private static Map<String, List<Double>> responseFileMapping = new HashMap<String, List<Double>>();
	
	public static JsonObject getGeolocationInJson(ServletContext servletContext, int index, String clientKey) throws Exception {
		List<Double> latList = readGeolocation(servletContext, "/mockdata/geolocation-latitude-"+clientKey+".txt");
		List<Double> lngList = readGeolocation(servletContext, "/mockdata/geolocation-longitude-"+clientKey+".txt");
		JsonObject geolocation = new JsonObject();
		geolocation.addProperty("latitude", latList.get(index-1));
		geolocation.addProperty("longitude", lngList.get(index-1));
		return geolocation;
	}
	
	public static JsonObject getDistanceInJson(double fromLat, double fromLon, double toLat, double toLon) {
		final int R = 6371; // Radius of the earth

	    Double latDistance = Math.toRadians(toLat - fromLat);
	    Double lonDistance = Math.toRadians(toLon - fromLon);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(fromLat)) * Math.cos(Math.toRadians(toLat))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = 0.0;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    double finalDistance = Math.sqrt(distance);
		
	    JsonObject distanceResult = new JsonObject();
	    distanceResult.addProperty("km", finalDistance/1000);
	    distanceResult.addProperty("miles", finalDistance*10/16000);
		
		return distanceResult;
	}
	
	public static List<Double> readGeolocation(ServletContext servletContext, String responseFile) throws Exception {
		InputStream inputStream = null;
		if (responseFileMapping.get(responseFile)!=null && responseFileMapping.get(responseFile).size()>0){
			return responseFileMapping.get(responseFile);
		}
        try {
            inputStream = servletContext.getResourceAsStream(responseFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<Double> list = new ArrayList<Double>();
    		String line;
    		
    		while((line = bufferedReader.readLine())!= null){
    		    list.add(Double.parseDouble(line));
    		}
    		responseFileMapping.put(responseFile, list);
            return list;
        } finally {
            if (inputStream != null) {
               inputStream.close();
            }
        }
	}

	

	public static JsonObject getContactInJson(int index) {
		JsonObject contact = new JsonObject();
		JsonArray websites = new JsonArray();
		JsonObject website_1 = new JsonObject();
		website_1.addProperty("identifier", "Main");
		website_1.addProperty("url", "http://www.nissanofportland.com/");
		JsonObject website_2 = new JsonObject();
		website_2.addProperty("identifier", "DealerSite");
		website_2.addProperty("url", "http://www.dummysite-"+index+".com/");
		websites.add(website_1);
		websites.add(website_2);
		contact.add("websites", websites);
		return contact;
	}

	public static JsonArray getAwardsInJson() {
		JsonArray awardsArray = new JsonArray();
		JsonObject award_01 = new JsonObject();
		award_01.addProperty("description", "Dealer of the Year award presented based on best after sales service.");
		award_01.addProperty("id", "AWD_01");
		award_01.addProperty("title", "Dealer of the Year");
		JsonObject award_02 = new JsonObject();
		award_02.addProperty("description", "Certified Service Center of Nissan.");
		award_02.addProperty("id", "AWD_02");
		award_02.addProperty("title", "Certified Service Center");
		awardsArray.add(award_01);
		awardsArray.add(award_02);
		return awardsArray;
	}

	public static JsonObject getAddressInJson(int index) {
		JsonObject address = new JsonObject();
		address.addProperty("addressLine1", "Address line 1 value");
		address.addProperty("addressLine2", "Address line 2 value some text");
		address.addProperty("addressLine3", "Address line 3 value some more text");
		address.addProperty("city", "Dummy city");
		address.addProperty("postalCode", "XXX"+index+"YYY");
		address.addProperty("state", "STATE_ZZZ");
		address.addProperty("stateCode", "Code-"+index);
		return address;
	}
}
