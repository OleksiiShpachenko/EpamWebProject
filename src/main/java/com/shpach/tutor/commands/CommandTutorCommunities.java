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
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.servise.CommunityService;
import com.shpach.tutor.servise.SessionServise;
import com.shpach.tutor.servise.TutorServices;
import com.shpach.tutor.servise.UserService;
import com.shpach.tutor.view.service.TutorMenu;

public class CommandTutorCommunities  implements ICommand {
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session == null)
			return page = Config.getInstance().getProperty(Config.LOGIN);
		checkSession = SessionServise.checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getUserByLogin((String) session.getAttribute("user"));
		
		Map<String,String[]> lastRequest=new HashMap<String, String[]>();
		lastRequest.putAll(request.getParameterMap());
		
	    request.getSession().setAttribute("lastRequest", lastRequest);
		
		List<TutorMenu> tutorMenu= TutorServices.getTutorMenu(user);
		TutorServices.setActiveMenuByCommand(tutorMenu, "tutorCommunities");
		request.setAttribute("menu", tutorMenu);
	
		List<Community> communities=CommunityService.getCommunitiesByUserWithTestsAndUsers(user);
		request.setAttribute("communities", communities);
		page = Config.getInstance().getProperty(Config.TUTOR_COMMUNITIES);
		return page;
	}

}
