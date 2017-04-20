package com.shpach.tutor.view.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.service.SessionServise;

/**
 * Servlet Filter implementation class StaticContentFilter
 * 
 * @author Shpachenko_A_K
 *
 */
public class StaticContentFilter implements Filter {
	private static final Logger logger = Logger.getLogger(StaticContentFilter.class);
	private ServletContext context;

	/**
	 * Default constructor.
	 */
	public StaticContentFilter() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		makeFilterLogic(request, response);
		chain.doFilter(request, response);
	}

	private void makeFilterLogic(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession(false);
		String url = httpServletRequest.getServletPath();

		boolean checkSession = false;

		if (session != null)
			checkSession = SessionServise.getInstance().checkSession(session.getId(), (String) session.getAttribute("user"));
		if (checkSession) {
			String indexPage = Config.getInstance().getProperty(Config.INDEX);
			if (url.equals(indexPage)) {
				try {
					String autoLoginSubmit = Config.getInstance().getProperty(Config.AUTO_SUBMIT_LOGIN);
					httpServletRequest.getRequestDispatcher(autoLoginSubmit).forward(httpServletRequest, response);
				} catch (ServletException e) {
					logger.error(e, e);
				} catch (IOException e) {
					logger.error(e, e);
				}
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
	}

}
