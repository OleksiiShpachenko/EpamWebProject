package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.servise.CommunityService;
import com.shpach.tutor.servise.SessionServise;

public class CommandAssignTestToCommunity implements ICommand {
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
		//User user = UserService.getUserByLogin((String) session.getAttribute("user"));
	    
		String communityId=request.getParameter("communityId");
		String testId=request.getParameter("testId");
		
		
		boolean isOk=CommunityService.assignTestToCommunity(testId, communityId);
		request.setAttribute("assignTestToCommunityStatus", isOk);
	
		page ="/pages"; 
		return page;
	}


}
