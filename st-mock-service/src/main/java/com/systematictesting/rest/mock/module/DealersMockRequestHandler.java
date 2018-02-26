package com.systematictesting.rest.mock.module;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.systematictesting.comparator.DealerComparator;
import com.systematictesting.rest.core.controllers.AbstractController;
import com.systematictesting.utils.DealerUtils;

@Controller
@RequestMapping("/")
public class DealersMockRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(DealersMockRequestHandler.class);
	private static final String BRAND = "nissan";
	private static final String COUNTRY = "us";
	
	@Autowired
	private ServletContext servletContext;
	
	@RequestMapping(value = "/dealers", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody
	Object getDealers(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String requestPath = request.getServletPath();
		String clientkey = request.getHeader("clientKey");
		logger.debug("HANDLING "+requestPath+"/dealers REQUEST :: GET :: "+clientkey);
		String location = request.getParameter("location");
		double fromLat = 40.7127837;
		double fromLng = -74.0059413;
		if (location!=null){
			URL url = new URL("http://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(location,"UTF-8") + "&sensor=true");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Accept", "application/json");

	        if (conn.getResponseCode() != 200) {
	            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	        }
	        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

	        String output = "";
	        String full = "";
	        while ((output = br.readLine()) != null) {
	            full += output;
	        }
	        
	        JsonParser parser = new JsonParser();
	        JsonObject location_response = parser.parse(full).getAsJsonObject();
	        fromLat = ((JsonObject)location_response.getAsJsonArray("results").get(0)).getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
	        fromLng = ((JsonObject)location_response.getAsJsonArray("results").get(0)).getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();

		}
		
		
		String size = request.getParameter("size");
		int count = 10;
		try{
			count = Integer.parseInt(size);
			if (count==-1){
				count = 5000;
			}
		} catch (NumberFormatException e){
			logger.error("NumberFormatException generated on Size Parameters : ",e);
		}
		boolean toggleBooleanTypeValue = true;
		JsonArray allDealers = new JsonArray();
		List<JsonObject> allDealersInList = new ArrayList<JsonObject>();
		
		for (int index=1;index<=count;index++){
			toggleBooleanTypeValue = !toggleBooleanTypeValue;
			JsonObject dealer = new JsonObject();
			dealer.addProperty("urlId", BRAND+"Center_"+index);
			dealer.addProperty("type", toggleBooleanTypeValue?"R1":"R2");
			dealer.addProperty("siteId", "001");
			dealer.addProperty("name", "Dummy Dealer-"+index);
			dealer.addProperty("marketingGroupId", "101");
			dealer.addProperty("id", COUNTRY+"_"+BRAND+"_"+index+"-001");
			dealer.addProperty("hasDealerWebsite", toggleBooleanTypeValue);
			dealer.addProperty("dealerId", index);
			dealer.addProperty("canAcceptLeads", toggleBooleanTypeValue);
			dealer.add("address", DealerUtils.getAddressInJson(index));
			dealer.add("awards", DealerUtils.getAwardsInJson());
			dealer.add("contact", DealerUtils.getContactInJson(index));
			dealer.add("geolocation", DealerUtils.getGeolocationInJson(servletContext, index, clientkey));
			dealer.add("distance", DealerUtils.getDistanceInJson(fromLat, fromLng, dealer.getAsJsonObject("geolocation").get("latitude").getAsDouble(), dealer.getAsJsonObject("geolocation").get("longitude").getAsDouble()));
			allDealersInList.add(dealer);
		}
		Collections.sort(allDealersInList, new DealerComparator());
		for (JsonObject dealer: allDealersInList){
			allDealers.add(dealer);
		}
		JsonObject responseObject = new JsonObject();
		responseObject.add("dealers", allDealers);
		responseObject.addProperty("totalResults", 5000);
		Thread.sleep(2000);
		return responseObject;
	}

	
	
	@RequestMapping(value = "/dealers", method = RequestMethod.OPTIONS, produces = { "application/json" })
	public @ResponseBody
	void getDealersOptions(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING "+requestPath+"/dealers REQUEST :: OPTIONS");
	}
	
	@RequestMapping(value = "/dealers/{dealerIdentifier}", method = RequestMethod.OPTIONS, produces = { "application/json" })
	public @ResponseBody
	void getDealerOptions(HttpServletRequest request, HttpServletResponse response, @PathVariable String dealerIdentifier) throws UnsupportedEncodingException {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING "+requestPath+"/dealers/"+dealerIdentifier+" REQUEST :: OPTIONS");
	}
		
	@RequestMapping(value = "/dealers/{dealerIdentifier}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody
	Object getDealer(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer dealerIdentifier) throws Exception{
		String requestPath = request.getServletPath();
		logger.debug("HANDLING "+requestPath+"/dealer/"+dealerIdentifier+" REQUEST :: GET");
		int index = dealerIdentifier; 
		String clientkey = request.getHeader("clientKey");
		boolean toggleBooleanTypeValue = dealerIdentifier%2==0?true:false;
		JsonObject dealer = new JsonObject();
		dealer.addProperty("urlId", BRAND+"Center_"+index);
		dealer.addProperty("type", toggleBooleanTypeValue?"R1":"R2");
		dealer.addProperty("siteId", "001");
		dealer.addProperty("name", "Dummy Dealer-"+index);
		dealer.addProperty("marketingGroupId", "101");
		dealer.addProperty("id", COUNTRY+"_"+BRAND+"_"+index+"-001");
		dealer.addProperty("hasDealerWebsite", toggleBooleanTypeValue);
		dealer.addProperty("dealerId", index);
		dealer.addProperty("canAcceptLeads", toggleBooleanTypeValue);
		dealer.add("address", DealerUtils.getAddressInJson(index));
		dealer.add("awards", DealerUtils.getAwardsInJson());
		dealer.add("geolocation", DealerUtils.getGeolocationInJson(servletContext, index,clientkey));
		
		JsonObject contact = DealerUtils.getContactInJson(index);
		contact.addProperty("email", "dummy-dealer-"+index+"@test.com");
		contact.addProperty("fax", "9990000000"+index);
		JsonArray phones = new JsonArray();
		JsonObject phone1 = new JsonObject();
		phone1.addProperty("identifier", "sales");
		phone1.addProperty("number", "0200000000000"+index);
		JsonObject phone2 = new JsonObject();
		phone2.addProperty("identifier", "services");
		phone2.addProperty("number", "0200000000000"+index);
		phones.add(phone1);
		phones.add(phone2);
		contact.add("phones", phones);
		dealer.add("contact", contact);
		dealer.add("openingHours", getOpeningHoursInJson());
		
		JsonArray services = new JsonArray();
		for (int serviceIndex=1;serviceIndex<=100;serviceIndex++){
			JsonObject service = new JsonObject();
			service.addProperty("id", "fr_renault_"+serviceIndex);
			service.addProperty("serviceId", serviceIndex+"");
			service.addProperty("url", "http://www.google.com");
			services.add(service);
		}

		JsonObject us_en_nissan_A_OF = new JsonObject();
		us_en_nissan_A_OF.addProperty("id", "us_en_nissan_A_OF");
		us_en_nissan_A_OF.addProperty("serviceId", "us_en_nissan_A_OF");
		services.add(us_en_nissan_A_OF);
		
		JsonObject us_en_nissan_C_ICO = new JsonObject();
		us_en_nissan_C_ICO.addProperty("id", "us_en_nissan_C_ICO");
		us_en_nissan_C_ICO.addProperty("serviceId", "us_en_nissan_C_ICO");
		services.add(us_en_nissan_C_ICO);
		
		JsonObject us_en_nissan_C_CP = new JsonObject();
		us_en_nissan_C_CP.addProperty("id", "us_en_nissan_C_CP");
		us_en_nissan_C_CP.addProperty("serviceId", "us_en_nissan_C_CP");
		services.add(us_en_nissan_C_CP);
		
		JsonObject us_en_nissan_S_SVC = new JsonObject();
		us_en_nissan_S_SVC.addProperty("id", "us_en_nissan_S_SVC");
		us_en_nissan_S_SVC.addProperty("serviceId", "us_en_nissan_S_SVC");
		services.add(us_en_nissan_S_SVC);
		
		JsonObject us_en_nissan_S_SLS = new JsonObject();
		us_en_nissan_S_SLS.addProperty("id", "us_en_nissan_S_SLS");
		us_en_nissan_S_SLS.addProperty("serviceId", "us_en_nissan_S_SLS");
		services.add(us_en_nissan_S_SLS);
		dealer.add("services", services);
		
		return dealer;
	}

	private JsonObject getOpeningHoursInJson() {
		JsonObject openingHours = new JsonObject();
		openingHours.addProperty("openingHoursText", "Monday-Friday: 08:00-19:00\r\nSaturday: 09:00-13:00\r\nSunday: -\r\n");
		openingHours.addProperty("hasAfternoonHours", false);
		JsonArray regularOpeningHours = new JsonArray();
		
		JsonObject regularOpeningHoursSub1 = new JsonObject();
		regularOpeningHoursSub1.addProperty("weekDay", 1);
		regularOpeningHoursSub1.addProperty("closed", false);
		JsonArray openIntervals1 = new JsonArray();
		JsonObject openInterval1 = new JsonObject();
		openInterval1.addProperty("openFrom", "08:00");
		openInterval1.addProperty("openUntil", "17:00");
		openIntervals1.add(openInterval1);
		regularOpeningHoursSub1.add("openIntervals", openIntervals1);
		
		JsonObject regularOpeningHoursSub2 = new JsonObject();
		regularOpeningHoursSub2.addProperty("weekDay", 2);
		regularOpeningHoursSub2.addProperty("closed", false);
		JsonArray openIntervals2 = new JsonArray();
		JsonObject openInterval21 = new JsonObject();
		openInterval21.addProperty("openFrom", "08:00");
		openInterval21.addProperty("openUntil", "12:00");
		JsonObject openInterval22 = new JsonObject();
		openInterval22.addProperty("openFrom", "13:00");
		openInterval22.addProperty("openUntil", "17:00");
		openIntervals2.add(openInterval21);
		openIntervals2.add(openInterval22);
		regularOpeningHoursSub2.add("openIntervals", openIntervals2);
		
		JsonObject regularOpeningHoursSub3 = new JsonObject();
		regularOpeningHoursSub3.addProperty("weekDay", 3);
		regularOpeningHoursSub3.addProperty("closed", false);
		JsonArray openIntervals3 = new JsonArray();
		JsonObject openInterval3 = new JsonObject();
		openInterval3.addProperty("openFrom", "08:00");
		openInterval3.addProperty("openUntil", "17:00");
		openIntervals3.add(openInterval3);
		regularOpeningHoursSub3.add("openIntervals", openIntervals3);
		
		JsonObject regularOpeningHoursSub4 = new JsonObject();
		regularOpeningHoursSub4.addProperty("weekDay", 4);
		regularOpeningHoursSub4.addProperty("closed", false);
		JsonArray openIntervals4 = new JsonArray();
		JsonObject openInterval4 = new JsonObject();
		openInterval4.addProperty("openFrom", "08:00");
		openInterval4.addProperty("openUntil", "17:00");
		openIntervals4.add(openInterval4);
		regularOpeningHoursSub4.add("openIntervals", openIntervals4);
		
		JsonObject regularOpeningHoursSub5 = new JsonObject();
		regularOpeningHoursSub5.addProperty("weekDay", 5);
		regularOpeningHoursSub5.addProperty("closed", false);
		JsonArray openIntervals5 = new JsonArray();
		JsonObject openInterval5 = new JsonObject();
		openInterval5.addProperty("openFrom", "09:00");
		openInterval5.addProperty("openUntil", "16:00");
		openIntervals5.add(openInterval5);
		regularOpeningHoursSub5.add("openIntervals", openIntervals5);
		
		JsonObject regularOpeningHoursSub6 = new JsonObject();
		regularOpeningHoursSub6.addProperty("weekDay", 6);
		regularOpeningHoursSub6.addProperty("closed", false);
		JsonArray openIntervals6 = new JsonArray();
		JsonObject openInterval6 = new JsonObject();
		openInterval6.addProperty("openFrom", "10:00");
		openInterval6.addProperty("openUntil", "18:00");
		openIntervals6.add(openInterval6);
		regularOpeningHoursSub6.add("openIntervals", openIntervals6);
		
		JsonObject regularOpeningHoursSub7 = new JsonObject();
		regularOpeningHoursSub7.addProperty("weekDay", 7);
		regularOpeningHoursSub7.addProperty("closed", true);
		JsonArray openIntervals7 = new JsonArray();
		JsonObject openInterval7 = new JsonObject();
		openInterval7.addProperty("openFrom", "06:00");
		openInterval7.addProperty("openUntil", "23:00");
		openIntervals7.add(openInterval7);
		regularOpeningHoursSub7.add("openIntervals", openIntervals7);

		
		regularOpeningHours.add(regularOpeningHoursSub1);
		regularOpeningHours.add(regularOpeningHoursSub2);
		regularOpeningHours.add(regularOpeningHoursSub3);
		regularOpeningHours.add(regularOpeningHoursSub4);
		regularOpeningHours.add(regularOpeningHoursSub5);
		regularOpeningHours.add(regularOpeningHoursSub6);
		regularOpeningHours.add(regularOpeningHoursSub7);
		openingHours.add("regularOpeningHours", regularOpeningHours);
		return openingHours;
	}
	
	@RequestMapping(value = "/dealers/services", method = RequestMethod.OPTIONS, produces = { "application/json" })
	public @ResponseBody
	void getDealersServicesOptions(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING "+requestPath+"/dealers/services REQUEST :: OPTIONS");
	}
	
	@RequestMapping(value = "/dealers/services", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody
	Object getDealersServices(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String requestPath = request.getServletPath();
		String clientkey = request.getHeader("clientKey");
		logger.debug("HANDLING "+requestPath+"/dealer/services REQUEST :: GET");
		
		String apiVersion = requestPath.substring(1);
		String responseFile = "/mockdata/"+apiVersion+"-dealers-services-"+clientkey+".json";
		
		try {
			return readAFile(servletContext, responseFile);
		} catch (Exception e) {
			logger.error("Mock Response file doesn't exist :: "+responseFile);
			logger.error("Exception : ",e);
			throw new Exception("Mock Response file doesn't exist :: "+responseFile);
		}
	}
}
