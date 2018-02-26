package com.systematictesting.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.systematictesting.daolayer.beans.Feature;
import com.systematictesting.daolayer.beans.License;
import com.systematictesting.daolayer.constants.UserConstants;
import com.systematictesting.daolayer.entity.User;
import com.systematictesting.rest.core.utils.EncryptionHandler;
import com.systematictesting.services.core.LicenseService;

public class LicenseFilter implements Filter{
	
	private static final Logger logger = LoggerFactory.getLogger(LicenseFilter.class);
	
	private LicenseService licenseService;

	@Override
	public void destroy() {
	}

	@SuppressWarnings("deprecation")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (licenseService==null){
			logger.warn("licenseService is detected null.");
			licenseService = (LicenseService) RequestContextUtils.getWebApplicationContext(request).getBean(LicenseService.SERVICE_NAME);
			if (licenseService!=null){
				logger.warn("licenseService is restored.");
			}
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		User user = getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(httpRequest);
		if (user!=null && user.getEmail()!=null && user.getEmail().length()>0){
			Map<String,Long> featureList = licenseService.getAvailableFeatures(user.getEmail());
			logger.debug("LicenseFilter :: FEATURE LIST OF REQUESTED EMAIL : "+user.getEmail()+" :: "+featureList);
			httpRequest.setAttribute(License.FIELDS.FEATURE_LIST, featureList);
			httpRequest.setAttribute(License.FEATURE_KEY, new Feature());
			httpRequest.setAttribute(User.FIELDS.EMAIL, user.getEmail());
			chain.doFilter(request, response);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("LicenseService :: Enter");
		licenseService = (LicenseService) WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext()).getBean(LicenseService.SERVICE_NAME);
		logger.debug("LicenseService :: licenseService="+licenseService);
		logger.debug("LicenseService :: Exit");
	}
	
	private User getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey(HttpServletRequest request) throws UnsupportedEncodingException {
		String encodedAccountId = getValueFromRequestCookies(request, UserConstants.ACCOUNT_ID);
		String sessionId = getValueFromRequestCookies(request, UserConstants.SESSION_ID);
		String defaultAPIKey = getValueFromRequestCookies(request, User.FIELDS.DEFAULT_API_KEY);
		if (encodedAccountId!=null && sessionId!=null && defaultAPIKey!=null){
			String accountId = URLDecoder.decode(encodedAccountId,UserConstants.UTF_8_ENCODING);
			if (logger.isDebugEnabled()){
				logger.debug("REQUESTED ACCOUNT ID = " + accountId);
				logger.debug("REQUESTED SESSION ID = " + sessionId);
				logger.debug("REQUESTED DEFAULT API KEY = " + defaultAPIKey);
			}
			String email = EncryptionHandler.decrypt(accountId, sessionId + UserConstants.GENERIC_PASSWORD_KEY);
			logger.debug("DECRYPTED EMAIL on getUserFromReqUsingAccountIdSessionIdAndDefaultAPIKey() = " + email);
			User user = new User();
			user.setEmail(email);
			user.setDefaultAPIkey(defaultAPIKey);
			return user;
		} else {
			return null;
		}
		
	}
	
	private String getValueFromRequestCookies(HttpServletRequest request, String cookieName){
		String value = null;
		if (request.getCookies()!=null){
			for(Cookie cookie : request.getCookies()){
				if (cookie.getName().equals(cookieName)){
					value = cookie.getValue();
					break;
				}
			}
		}
		return value;
	}
}
