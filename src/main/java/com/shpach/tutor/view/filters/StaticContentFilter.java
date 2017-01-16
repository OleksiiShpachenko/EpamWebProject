package com.shpach.tutor.view.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.servise.SessionServise;

/**
 * Servlet Filter implementation class StaticContentFilter
 */
public class StaticContentFilter implements Filter {
	private ServletContext context;

	/**
	 * Default constructor.
	 */
	public StaticContentFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		// HttpServletRequest httpServletRequest=(HttpServletRequest) request;
		// HttpSession session = httpServletRequest.getSession(false);
		// String url= httpServletRequest.getServletPath();
		// pass the request along the filter chain
		makeFilterLogic(request, response);
		chain.doFilter(request, response);
	}

	private void makeFilterLogic(ServletRequest request, ServletResponse response) {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpSession session = httpServletRequest.getSession(false);
		String url = httpServletRequest.getServletPath();
		String param=httpServletRequest.getContextPath();//.getParameter("command");
    	String param1=httpServletRequest.getServletPath();
    	String param2=httpServletRequest.getPathInfo();
    	String param3=httpServletRequest.getPathTranslated();//.getPathInfo();

		boolean checkSession = false;
		
		if (session != null)
			checkSession = SessionServise.checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession){
			// TODO: check if not permited resourses,
			int a = 1;
		} else {
			String indexPage = Config.getInstance().getProperty(Config.INDEX);
			if (url.equals(indexPage)) {
				try {
					String autoLoginSubmit = Config.getInstance().getProperty(Config.AUTO_SUBMIT_LOGIN);
					httpServletRequest.getRequestDispatcher(autoLoginSubmit).forward(httpServletRequest,
							response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/*
		 * String userar=null; if(session != null &&
		 * session.getAttribute("user") != null) userar=(String)
		 * session.getAttribute("user"); if (session == null ||
		 * session.getAttribute("user") == null) {
		 * 
		 * // TODO: check if not permited resourses, int a = 1; } else { String
		 * indexPage=Config.getInstance().getProperty(Config.INDEX); if
		 * (url.equals(indexPage)) { HttpServletRequestWrapper wrap=new
		 * HttpServletRequestWrapper(httpServletRequest); //wrap.set
		 * //httpServletRequest.setAttribute("command", "login"); String
		 * param=httpServletRequest.getParameter("command"); try {
		 * httpServletRequest.getRequestDispatcher("/pages/?command=login").
		 * forward(httpServletRequest, response); } catch (ServletException e) {
		 * // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } Enumeration<String> params =
		 * httpServletRequest.getParameterNames();
		 * while(params.hasMoreElements()){ String name = params.nextElement();
		 * String value = request.getParameter(name);
		 * this.context.log(httpServletRequest.getRemoteAddr() +
		 * "::Request Params::{"+name+"="+value+"}"); }
		 * 
		 * } }
		 */

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */

	public void init(FilterConfig fConfig) throws ServletException {
		this.context = fConfig.getServletContext();
		// this.context.log("AuthenticationFilter initialized");
	}

}
