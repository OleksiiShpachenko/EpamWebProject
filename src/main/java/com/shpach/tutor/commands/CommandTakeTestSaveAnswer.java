package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.shpach.tutor.manager.Config;
import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.service.SessionServise;

/**
 * Command which add {@link AnswersLog} to the {@link Task} and save it in
 * session while Student take test
 * 
 * @author Shpachenko_A_K
 *
 */
public class CommandTakeTestSaveAnswer implements ICommand {
	private static final Logger logger = Logger.getLogger(CommandTakeTestSaveAnswer.class);
	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = "/pages";
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

		Integer testQuestionBankIndex = (Integer) request.getSession().getAttribute("testQuestionBankIndex");
		Task task = (Task) request.getSession().getAttribute("task");
		List<String> checkedIndx = Arrays.asList(request.getParameterValues("answerCheckedIndx"));

		if (task == null || testQuestionBankIndex == null || checkedIndx == null){
			logger.error("error in sessions attributes");
			return Config.getInstance().getProperty(Config.ERROR);
		}

		QuestionLog questionLog = new QuestionLog();
		TestQuestionsBank testQuestionsBank = task.getTest().getTestQuestionsBanks().get(testQuestionBankIndex);
		questionLog.setQuestion(testQuestionsBank.getQuestion());
		questionLog.setQuestionLogSortingOrder(testQuestionsBank.getQuestionDefaultSortingOrder());

		List<Answer> answers = testQuestionsBank.getQuestion().getAnswers();
		int i = 0;
		for (Answer answer : answers) {
			AnswersLog answLog = new AnswersLog();
			answLog.setAnswer(answer);
			answLog.setAnswerLogSortingOrder(answer.getAnswerDefaultSortingOrder());
			byte isChecked = checkedIndx.contains(Integer.toString(i)) ? (byte) 1 : 0;
			answLog.setAnswerChecked(isChecked);
			questionLog.addAnswersLog(answLog);
			i++;
		}
		task.addQuestionLog(questionLog);
		request.getSession().setAttribute("task", task);
		Boolean lastQuestion = (Boolean) request.getSession().getAttribute("testLastQuestion");
		if (lastQuestion == null)
			page = new CommandTakeTestShowQuestion().execute(request, responce);

		return page;

	}

}
