package com.bigrez.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import weblogic.logging.LoggingHelper;

/** AuditFilter is a simple Filter used to log page requests */
public class AuditFilter implements javax.servlet.Filter {

	final Logger logger = LoggingHelper.getServerLogger();
	private FilterConfig myFilterConfig;

	public FilterConfig getFilterConfig() {
		return myFilterConfig;
	}

	public void init(FilterConfig pValue) {
		myFilterConfig = pValue;
	}

	public void destroy() { }


	/**
	 *  Main filter processing method. Prints selected request information to the logger.
	 *
	 * @param  request
	 * @param  response
	 * @param  chain
	 * @exception  IOException
	 * @exception  ServletException  
	 */
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain chain)
			 throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		StringBuffer auditentry = new StringBuffer();
		auditentry.append(req.getRemoteAddr() + " " + req.getRemoteUser() + " " + req.getRequestURI());

		Enumeration<String> e = req.getParameterNames();
		if (e.hasMoreElements()) {
			while (e.hasMoreElements()) {
				String name = e.nextElement();
				auditentry.append(" " + name + "=" + req.getParameter(name));
			}
		}

		logger.log(Level.INFO, auditentry.toString());

		// continue processing any other filters
		chain.doFilter(request, response);
	}
}

