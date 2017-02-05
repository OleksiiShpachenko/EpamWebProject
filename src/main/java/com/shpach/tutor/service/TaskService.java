package com.shpach.tutor.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.task.ITaskDao;

/**
 * Collection of services for {@link Task} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class TaskService {
	public static final int EXLUDABLE_AVERAGE = -1;

	/**
	 * Add {@link Task} with collection of {@link QuestionsLog} and
	 * {@link AnswersLog} using transaction
	 * 
	 * @param task
	 *            - {@link Task}
	 * @return true if transaction commits
	 */
	public static boolean addTaskWithQuestionsLogAndAnswersLog(Task task) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITaskDao taskDao = daoFactory.getTaskDao();
		//ConnectionPool pool = ConnectionPool.getInstance();
		Connection connection = null;
		try {
			connection =ConnectionPool.getConnection();// pool.getConnection();
			connection.setAutoCommit(false);
			Task taskAdded = taskDao.addOrUpdate(task, connection);
			if (taskAdded == null) {
				connection.rollback();
				return false;
			}
			for (QuestionLog questionLog : task.getQuestionLogs()) {
				if (QuestionLogService.addQuestionLog(questionLog, connection) == null) {
					connection.rollback();
					return false;
				}
				;
				for (AnswersLog answersLog : questionLog.getAnswersLogs()) {
					if (AnswersLogService.addAnswersLog(answersLog, connection) == null) {
						connection.rollback();
						return false;
					}
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Calculate score of {@link Task} according to users answers
	 * 
	 * @param task
	 *            {@link Task} with collection of {@link QuestionsLog} and
	 *            {@link AnswersLog}
	 * @return total score in percents
	 */
	public static byte calculateTascScore(Task task) {
		int maxValue = 0;
		int currentValue = 0;
		for (QuestionLog questionLog : task.getQuestionLogs()) {
			int questionScore = 0;
			for (AnswersLog answerLog : questionLog.getAnswersLogs()) {
				Answer answer = answerLog.getAnswer();
				maxValue += answer.getAnswerCorrect();
				if (answerLog.getAnswerChecked() == answer.getAnswerCorrect())
					questionScore += answer.getAnswerCorrect();
				else
					questionScore--;
			}
			currentValue += (questionScore < 0) ? 0 : questionScore;
		}
		int res = currentValue * 100 / maxValue;
		return (byte) res;
	}

	/**
	 * Insert collection of {@link Task} to collection of {@link Test} assigned
	 * to {@link User}
	 * 
	 * @param tests
	 *            - collection of {@link Test}
	 * @param user
	 *            - {@link User}
	 */
	public static void insertTasksToTestsListByUser(List<Test> tests, User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITaskDao taskDao = daoFactory.getTaskDao();
		if (tests == null)
			return;
		for (Test test : tests) {
			List<Task> tasks = taskDao.findTaskByTestIdAndUserId(test.getTestId(), user.getUserId());
			test.setTasks(tasks);
		}

	}

	/**
	 * Calculate average score of collection of {@link Task}
	 * 
	 * @param tasks
	 *            - collection of {@link Task}
	 * @return average score in percents
	 */
	public static int CalculateAverageScore(List<Task> tasks) {
		if (tasks == null || tasks.size() == 0)
			return EXLUDABLE_AVERAGE;
		OptionalDouble res = tasks.stream().mapToInt((c) -> c.getTaskScore()).average();
		if (res.isPresent())
			return (int) res.getAsDouble();
		return 0;
	}

	/**
	 * Insert collection of {@link Task} to collection of {@link Test}
	 * 
	 * @param tests
	 *            - collection of {@link Test}
	 */
	public static void insertTasksToTestsList(List<Test> tests) {
		if (tests == null)
			return;
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ITaskDao taskDao = daoFactory.getTaskDao();
		for (Test test : tests) {
			List<Task> tasks = taskDao.findTaskByTestId(test.getTestId());
			test.setTasks(tasks);
		}
	}

}
