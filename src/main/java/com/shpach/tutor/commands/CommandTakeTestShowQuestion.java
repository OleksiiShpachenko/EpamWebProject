package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.service.SessionServise;

/**
 * Command which set the next index of question (for show it on the page) while
 * Student take test
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTakeTestShowQuestion implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTakeTestShowQuestion.class);

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
		Integer testQuestionBankIndex = (Integer) request.getSession().getAttribute("testQuestionBankIndex");
		Task task = (Task) request.getSession().getAttribute("task");
		if (task == null) {
			logger.error("no Task attribute in session");
			return Config.getInstance().getProperty(Config.ERROR);
		}
		if (testQuestionBankIndex == null) {
			testQuestionBankIndex = new Integer(0);
		} else {
			testQuestionBankIndex++;
			if (task.getTest().getTestQuestionsBanks().size() <= testQuestionBankIndex) {
				logger.error("error testQuestionBankIndex");
				return Config.getInstance().getProperty(Config.ERROR);
			}
			if ((task.getTest().getTestQuestionsBanks().size() - 1) == testQuestionBankIndex)
				request.getSession().setAttribute("testLastQuestion", true);
		}
		request.getSession().setAttribute("testQuestionBankIndex", testQuestionBankIndex);
		page = Config.getInstance().getProperty(Config.TAKE_TEST);
		return page;

	}

}
