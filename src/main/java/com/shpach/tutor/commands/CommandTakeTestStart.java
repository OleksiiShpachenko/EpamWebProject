package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.TestService;
import com.shpach.tutor.service.UserService;

/**
 * Command which create new {@link Task}, save it in session and start taking
 * test by Student
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTakeTestStart implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTakeTestStart.class);

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
		int testId = 0;
		try {
			testId = Integer.parseInt(request.getParameter("testId"));
			if (testId < 1)
				return Config.getInstance().getProperty(Config.ERROR);
		} catch (NumberFormatException ex) {
			return Config.getInstance().getProperty(Config.ERROR);
		}
		Test test = TestService.getTestByIdWhithQuestionsAndAnswers(testId);
		Task task = new Task();
		task.setUserId(user.getUserId());
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		request.getSession().setAttribute("task", task);

		page = new CommandTakeTestShowQuestion().execute(request, responce);
		return page;

	}

}
