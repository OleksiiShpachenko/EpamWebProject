package com.shpach.tutor.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPoolTomCatFactory;
import com.shpach.tutor.persistance.jdbc.connection.IConnectionPoolFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.task.ITaskDao;

/**
 * Service layer for {@link Task} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class TaskService {
	public static final int EXLUDABLE_AVERAGE = -1;
	private static TaskService instance = null;
	private ITaskDao taskDao;
	private IConnectionPoolFactory connectionFactory;
	private QuestionLogService questionLogService;
	private AnswersLogService answersLogService;
	private TestService testService;

	private TaskService() {

	}

	public static synchronized TaskService getInstance() {
		if (instance == null) {
			instance = new TaskService();
		}
		return instance;
	}

	private ITaskDao getTaskDao() {
		if (taskDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			taskDao = daoFactory.getTaskDao();
		}
		return taskDao;
	}

	private QuestionLogService getQuestionLogService() {
		if (questionLogService == null)
			questionLogService = QuestionLogService.getInstance();
		return questionLogService;
	}

	private AnswersLogService getAnswersLogService() {
		if (answersLogService == null)
			answersLogService = AnswersLogService.getInstance();
		return answersLogService;
	}

	public IConnectionPoolFactory getConnectionFactory() {
		if (connectionFactory == null) {
			connectionFactory = (IConnectionPoolFactory) new ConnectionPoolTomCatFactory();
		}
		return connectionFactory;
	}

	public TestService getTestService() {
		if (testService == null)
			testService = TestService.getInstance();
		return testService;
	}

	/**
	 * Add {@link Task} with collection of {@link QuestionsLog} and
	 * {@link AnswersLog} using transaction
	 * 
	 * @param task
	 *            - {@link Task}
	 * @return true if transaction commits
	 */
	public boolean addTaskWithQuestionsLogAndAnswersLog(Task task) {
		if (task == null)
			return false;
		Connection connection = null;
		try {
			connection = getConnectionFactory().getConnection();
			connection.setAutoCommit(false);
			Task taskAdded = getTaskDao().addOrUpdate(task, connection);
			if (taskAdded == null) {
				connection.rollback();
				return false;
			}
			for (QuestionLog questionLog : task.getQuestionLogs()) {
				if (getQuestionLogService().addQuestionLog(questionLog, connection) == null) {
					connection.rollback();
					return false;
				}
				for (AnswersLog answersLog : questionLog.getAnswersLogs()) {
					if (getAnswersLogService().addAnswersLog(answersLog, connection) == null) {
						connection.rollback();
						return false;
					}
				}
			}
			return true;
		} catch (SQLException | NullPointerException e) {
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
	public byte calculateTascScore(Task task) {
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
	public void insertTasksToTestsListByUser(List<Test> tests, User user) {
		if (tests == null || user == null)
			return;
		for (Test test : tests) {
			List<Task> tasks = getTaskDao().findTaskByTestIdAndUserId(test.getTestId(), user.getUserId());
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
	public int CalculateAverageScore(List<Task> tasks) {
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
	public void insertTasksToTestsList(List<Test> tests) {
		if (tests == null)
			return;
		for (Test test : tests) {
			List<Task> tasks = getTaskDao().findTaskByTestId(test.getTestId());
			test.setTasks(tasks);
		}
	}

	/**
	 * Get collection of {@link Task} by {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity
	 */
	public List<Task> getTasksByUser(User user) {
		List<Task> empty = new ArrayList<>();
		if (user == null)
			return empty;
		List<Task> res = getTaskDao().findTaskByUserId(user.getUserId());
		if (res == null)
			return empty;
		return res;
	}

	public int getMinScore(List<Task> tasks) {
		int min = Integer.MAX_VALUE;
		if (tasks==null)
			return min;
		for (Task task : tasks) {
			if (task.getTaskScore() < min)
				min = task.getTaskScore();
		}
		return min;
	}

	public Test getHardestTestByTasksScore() {
		List<Task> tasks = getTaskDao().findAllTasks();
		if (tasks==null)
			return null;
		Set<Integer> tetsIdUnique = new HashSet<>();
		for (Task task : tasks) {
			tetsIdUnique.add(task.getTestId());
		}
		int hardestTestId = 0;
		int minValue = Integer.MAX_VALUE;
		for (Integer testId : tetsIdUnique) {
			OptionalDouble count = tasks.stream().filter(c -> c.getTestId() == testId).mapToInt(c -> c.getTaskScore())
					.average();
			if (count.isPresent()) {
				int average = (int) count.getAsDouble();
				if (average < minValue) {
					minValue = average;
					hardestTestId = testId;
				}
			}
		}
		Test hardTest = getTestService().getTestById(hardestTestId);
		return hardTest;
	}
}
