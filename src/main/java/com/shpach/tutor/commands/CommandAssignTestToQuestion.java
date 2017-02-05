package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.TestQuestionsBankService;

/**
 * Command which assign Test to question
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandAssignTestToQuestion implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandAssignTestToQuestion.class);
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

		String questionId = request.getParameter("questionId");
		String testId = request.getParameter("testId");

		boolean isOk = TestQuestionsBankService.assignTestToQuestion(testId, questionId);
		request.setAttribute("assignTestToQuestionStatus", isOk);

		page = "/pages";
		return page;
	}

}
