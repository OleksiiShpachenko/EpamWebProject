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
import com.shpach.tutor.service.SessionServise;
import com.shpach.tutor.service.TaskService;

/**
 * Command which add {@link Task} (Students taking test result) to database
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTakeTestFinish implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTakeTestFinish.class);

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
		Task task = (Task) request.getSession().getAttribute("task");

		if (task == null) {
			logger.error("no Task attribute in session");
			return Config.getInstance().getProperty(Config.ERROR);
		}
		byte score = TaskService.calculateTascScore(task);
		task.setTaskScore(score);
		task.setTaskDatetimeStop(new Date());
		boolean isOk = TaskService.addTaskWithQuestionsLogAndAnswersLog(task);
		if (isOk) {
			request.setAttribute("taskScore", score);
		}
		request.setAttribute("takeTestFinishStatus", isOk);
		request.getSession().removeAttribute("task");
		request.getSession().removeAttribute("testLastQuestion");
		request.getSession().removeAttribute("testQuestionBankIndex");
		page = "/pages";

		return page;

	}

}
