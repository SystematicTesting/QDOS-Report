package com.systematictesting.aop.trace.interceptors;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;

public class TraceInterceptor extends CustomizableTraceInterceptor {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(TraceInterceptor.class);

	@Override
	protected void writeToLog(Log log, String message, Throwable ex) {
		if (ex != null) {
			logger.debug(message, ex);
		} else {
			logger.debug(message);
		}
	}

	@Override
	protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger) {
		return true;
	}
}
