package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.servise.SessionServise;
import com.shpach.tutor.servise.TestService;
import com.shpach.tutor.servise.UserService;

public class CommandAssignTestToCategoryDialog implements ICommand {
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
			List<Test> tests = TestService.getTestsByUsers(user);
			TestService.insertCommunitiesToTests(tests);
			TestService.insertCategoriesToTests(tests);
			request.setAttribute("tests", tests); 
			//boolean isOk=CommunityService.assignUserToCommunity(userName, communityId);
			//request.setAttribute("assignUserStatus", isOk);
			request.setAttribute("assignTestToCategoryDialog", true);
			request.setAttribute("categoryId", request.getParameter("categoryId"));
			request.setAttribute("categoryName", request.getParameter("categoryName"));
		
			page ="/pages"; 
			return page;
		}

}
