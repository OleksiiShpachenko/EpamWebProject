package com.shpach.tutor.commands;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.TestService;
import com.shpach.tutor.service.UserService;

/**
 * Command which add new Test to database
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandNewTest implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandNewTest.class);

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
		String testName = request.getParameter("testName");
		String[] questionsId = request.getParameterValues("questionId[]");
		String testDescription = (request.getParameter("testDesription") != null)
				? request.getParameter("testDesription") : "";
		String testRndQuestion = (request.getParameter("testRndQuestion") != null)
				? request.getParameter("testRndQuestion") : "0";
		String testRndAnswer = (request.getParameter("testRndAnswer") != null) ? request.getParameter("testRndAnswer")
				: "0";
		String testType = (request.getParameter("testType") != null) ? request.getParameter("testType") : "0";
		boolean isOk = false;
		Test test = new Test();
		if (testName != "") {
			try {
				test.setTestName(testName);
				test.setTestDescription(testDescription);
				test.setTestRndQuestion((byte) Integer.parseInt(testRndQuestion));
				test.setTestRndAnswer((byte) Integer.parseInt(testRndAnswer));
				test.setTestType((byte) Integer.parseInt(testType));
				test.setTestActive((byte) 1);
				test.setUserId(user.getUserId());
				isOk = TestService.getInstance().addNewTestAndAssignQuestions(test, questionsId);
			} catch (NumberFormatException ex) {
				logger.error(ex, ex);
			}
		}
		request.setAttribute("addNewTestStatus", isOk);
		page = "/pages";
		return page;
	}

}
