package com.systematictesting.rest.mock.module;

import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.systematictesting.rest.core.controllers.AbstractController;

@Controller
@RequestMapping("/")
public class InventoryMockRequestHandler extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(UtilsMockRequestHandler.class);

	@Autowired
	private ServletContext servletContext;

	@RequestMapping(value = "/models/inventory/newcars", method = RequestMethod.POST, produces = { "application/json" })
	public @ResponseBody Object getNewCarInventory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING " + requestPath + "/models/inventory/newcars REQUEST :: GET");

		String apiVersion = requestPath.substring(1);
		
		String responseFile = "/mockdata/" + apiVersion + "-models-inventory-newcars.json";

		try {
			return readAFile(servletContext, responseFile);
		} catch (Exception e) {
			logger.error("Mock Response file doesn't exist :: " + responseFile);
			logger.error("Exception : ", e);
			throw new Exception("Mock Response file doesn't exist :: " + responseFile);
		}
	}
	
	

	@RequestMapping(value = "/models/inventory/newcars", method = RequestMethod.OPTIONS, produces = { "application/json" })
	public @ResponseBody void getNewCarInventoryOptions(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING " + requestPath + "/models/inventory/newcars REQUEST :: OPTIONS");
	}
	
	@RequestMapping(value = "/models/inventory/newcars/fields", method = RequestMethod.OPTIONS, produces = { "application/json" })
	public @ResponseBody void getNewCarInventoryFieldsOptions(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING " + requestPath + "/models/inventory/newcars/fields REQUEST :: OPTIONS");
	}
	
	@RequestMapping(value = "/models/inventory/newcars/fields", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody Object getNewCarInventoryFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String requestPath = request.getServletPath();
		logger.debug("HANDLING " + requestPath + "/models/inventory/newcars/fieldss REQUEST :: GET");

		String apiVersion = requestPath.substring(1);
		
		String responseFile = "/mockdata/" + apiVersion + "-models-inventory-newcars-fields.json";

		try {
			return readAFile(servletContext, responseFile);
		} catch (Exception e) {
			logger.error("Mock Response file doesn't exist :: " + responseFile);
			logger.error("Exception : ", e);
			throw new Exception("Mock Response file doesn't exist :: " + responseFile);
		}
	}
}
