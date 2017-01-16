/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.manager.Message;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.servise.LoginService;
import com.shpach.tutor.servise.SessionServise;
import com.shpach.tutor.servise.TutorServices;
import com.shpach.tutor.servise.UserService;
import com.shpach.tutor.view.service.TutorMenu;

/**
 *
 * @author MAXIM
 */
public class CommandLogin implements ICommand {

	private static final String LOGIN = "login";
	private static final String PASSWORD = "password";

	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session != null)
			checkSession = SessionServise.checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			String login = request.getParameter(LOGIN);
			String password = request.getParameter(PASSWORD);
			boolean checkLogin = LoginService.checkLogin(login, password);
			if (!checkLogin) {
				session.invalidate();
				
				request.setAttribute("errorLoginPassMessage", Message.getInstance().getProperty(Message.LOGIN_ERROR));
				return page = Config.getInstance().getProperty(Config.LOGIN);
			}
			request.getSession().setAttribute("user", login);
		}
		User user = UserService.getUserByLogin((String) session.getAttribute("user"));
		List<TutorMenu> tutorMenu= TutorServices.getTutorMenu(user);
		TutorServices.setActiveMenuByCommand(tutorMenu, "tests");
		request.setAttribute("menu", tutorMenu);
		//request.setAttribute("locale", "en_US");
		//request.getSession().setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", "ru-RU");
		//prepareRequestForTutorMainPage(request, user);
		//TODO: different page tutor and student
		
		Map<String,String[]> lastRequest=new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());
		
	    request.getSession().setAttribute("lastRequest", lastRequest);
		//Map<String,String[]> lastRequest2=(Map<String,String[]>)request.getSession().getAttribute("lastRequest");
		
		//String[] comm= lastRequest.get("command");// request.getParameter("command");
		//String comm2=((HttpServletRequest)request.getSession().getAttribute("lastRequest")).getParameter("command");
		page = "/pages";//Config.getInstance().getProperty(Config.TUTOR_TESTS);
		return page;
	}
	private void prepareRequestForTutorMainPage(HttpServletRequest request, User user){
		/*
		Map<String,Object> params=TutorServices.getParamForTutorMenu(user);
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			request.setAttribute(entry.getKey(), entry.getValue());
		}
		*/
	}

}
