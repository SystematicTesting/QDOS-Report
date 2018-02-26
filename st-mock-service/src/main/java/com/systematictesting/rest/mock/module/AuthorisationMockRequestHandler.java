package com.systematictesting.rest.mock.module;

import java.io.UnsupportedEncodingException;

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

import com.systematictesting.rest.core.controllers.AbstractController;

@Controller
@RequestMapping("/")
public class AuthorisationMockRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(AuthorisationMockRequestHandler.class);

	@Autowired
	private ServletContext servletContext;

	@RequestMapping(value = "/authorisation/{country}/{language}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Object getDealers(HttpServletRequest request, HttpServletResponse response, @PathVariable String country, @PathVariable String language) throws Exception {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING " + requestPath + "/authorisation/"+country+"/"+language+" REQUEST :: GET");

		String apiVersion = requestPath.substring(1);
		
		String responseFile = "/mockdata/" + apiVersion + "-authorisation-" + country + "-" + language + ".json";

		try {
			return readAFile(servletContext, responseFile);
		} catch (Exception e) {
			logger.error("Mock Response file doesn't exist :: " + responseFile);
			logger.error("Exception : ", e);
			throw new Exception("Mock Response file doesn't exist :: " + responseFile);
		}
	}

	@RequestMapping(value = "/authorisation/{country}/{language}", method = RequestMethod.OPTIONS, produces = { "application/json" })
	public @ResponseBody void getDealersOptions(HttpServletRequest request, HttpServletResponse response, @PathVariable String country, @PathVariable String language)
			throws UnsupportedEncodingException {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING " + requestPath + "/authorisation/"+country+"/"+language+" REQUEST :: OPTIONS");
	}
}
