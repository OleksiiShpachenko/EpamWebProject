package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.CategoryService;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.UserService;

/**
 * Command which sets request attributes to enable to show "assign category to
 * test dialog"
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandAssignCategoryToTestDialog implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandAssignCommunityToTestDialog.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = null;
		boolean checkSession = false;

		HttpSession session = request.getSession(false);

		if (session == null) {
			logger.warn("try to access without session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		checkSession = SessionServise.getInstance().checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			logger.warn("invalid session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getInstance().getUserByLogin((String) session.getAttribute("user"));

		List<Category> categories = CategoryService.getInstance().getCategoriesByUserWithTestsList(user);
		request.setAttribute("categories", categories);

		request.setAttribute("assignCategoryToTestDialog", true);
		request.setAttribute("testId", request.getParameter("testId"));
		request.setAttribute("testName", request.getParameter("testName"));

		page = "/pages";
		return page;
	}

}
