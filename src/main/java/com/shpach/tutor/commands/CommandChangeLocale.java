package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;

/**
 * Command which change locale and redirect to previous command
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandChangeLocale implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandChangeLocale.class);

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = Config.getInstance().getProperty(Config.LOGIN);
		String locale = request.getParameter("localeValue");
		Map<String, String[]> lastRequest = (HashMap<String, String[]>) request.getSession()
				.getAttribute("lastRequest");

		if (lastRequest != null) {
			page = "/pages";
		}
		request.getSession().setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", locale);
		return page;
	}
}