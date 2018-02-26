package com.systematictesting.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(MockFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		logger.debug("V2MockFilter.java :: doFilter() :: Applying response headers.");
		response.setHeader("Accept","*/*");
		response.setHeader("Accept-Encoding","gzip, deflate, sdch, br");
		response.setHeader("Accept-Language","en-US,en;q=0.8,hi;q=0.6");
		response.setHeader("Access-Control-Allow-Headers","origin, x-requested-with, accept, apikey, clientkey");
		response.setHeader("Access-Control-Allow-Methods","GET, PUT, POST, DELETE");
		response.setHeader("Access-Control-Allow-Origin","*");
		response.setHeader("Access-Control-Max-Age","3628800");
		response.setHeader("Access-Control-Request-Headers","accept, apikey, clientkey");
		response.setHeader("Access-Control-Request-Method","GET");
		chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {		
	}
	
	@Override
	public void destroy() {
	}


}
