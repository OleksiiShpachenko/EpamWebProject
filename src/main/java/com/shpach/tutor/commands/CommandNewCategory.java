package com.shpach.tutor.commands;

import java.io.IOException;

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
 * Command which add new Category to database
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandNewCategory implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandNewCategory.class);
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
		checkSession = SessionServise.checkSession(session.getId(), (String) session.getAttribute("user"));
		if (!checkSession) {
			session.invalidate();
			logger.warn("invalid session");
			return page = Config.getInstance().getProperty(Config.LOGIN);
		}
		User user = UserService.getUserByLogin((String) session.getAttribute("user"));
		String categoryName = request.getParameter("cat_name");
		Category category = new Category();
		category.setCategoryName(categoryName);
		category.setCategory_user_id(user.getUserId());
		category.setCategoryActive((byte) 1);
		boolean isOk = CategoryService.addNewCategory(category);
		request.setAttribute("addCategoryStatus", isOk);
		page = "/pages";
		return page;
	}

}
