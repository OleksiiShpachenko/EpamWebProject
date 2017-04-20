package Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.*;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.connection.IConnectionPoolFactory;
import com.shpach.tutor.persistance.jdbc.dao.task.ITaskDao;
import com.shpach.tutor.service.AnswersLogService;
import com.shpach.tutor.service.QuestionLogService;
import com.shpach.tutor.service.TaskService;
import com.shpach.tutor.service.TestService;
import TestUtils.TestUtils;

public class TaskServiceTest {
	private TaskService taskService;
	private ITaskDao mockTaskDao;
	private IConnectionPoolFactory mockConnectionFactory;
	private Connection mockConnection;
	private QuestionLogService mockQuestionLogService;
	private AnswersLogService mockAnswersLogService;
	private TestService mockTestService;

	@Before
	public void init() {
		taskService = TaskService.getInstance();

		mockTaskDao = Mockito.mock(ITaskDao.class);
		mockConnectionFactory = Mockito.mock(IConnectionPoolFactory.class);
		mockConnection = Mockito.mock(Connection.class);
		mockQuestionLogService = Mockito.mock(QuestionLogService.class);
		mockAnswersLogService = Mockito.mock(AnswersLogService.class);
		mockTestService = Mockito.mock(TestService.class);

		TestUtils.getInstance().mockPrivateField(taskService, "taskDao", mockTaskDao);
		TestUtils.getInstance().mockPrivateField(taskService, "connectionFactory", mockConnectionFactory);
		TestUtils.getInstance().mockPrivateField(taskService, "questionLogService", mockQuestionLogService);
		TestUtils.getInstance().mockPrivateField(taskService, "answersLogService", mockAnswersLogService);
		TestUtils.getInstance().mockPrivateField(taskService, "testService", mockTestService);
	}

	@Test
	public void addTaskWithQuestionsLogAndAnswersLog() throws SQLException {
		Task task = new Task();
		User user = new User();
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		task.setTaskDatetimeStop(new Date());

		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestion(new Question());

		AnswersLog answersLog = new AnswersLog();
		answersLog.setAnswer(new Answer());
		questionLog.addAnswersLog(answersLog);
		task.addQuestionLog(questionLog);

		when(mockConnectionFactory.getConnection()).thenReturn(mockConnection);
		when(mockTaskDao.addOrUpdate(task, mockConnection)).thenReturn(task);
		when(mockQuestionLogService.addQuestionLog(questionLog, mockConnection)).thenReturn(questionLog);
		when(mockAnswersLogService.addAnswersLog(answersLog, mockConnection)).thenReturn(answersLog);

		boolean res = taskService.addTaskWithQuestionsLogAndAnswersLog(task);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		verify(mockConnection, times(0)).rollback();
		verify(mockConnectionFactory, times(1)).getConnection();
		verify(mockTaskDao, times(1)).addOrUpdate(task, mockConnection);
		verify(mockQuestionLogService, times(1)).addQuestionLog(questionLog, mockConnection);
		verify(mockAnswersLogService, times(1)).addAnswersLog(answersLog, mockConnection);

		assertTrue(res);
	}

	@Test
	public void addTaskWithQuestionsLogAndAnswersLogFailNoAnswerLog() throws SQLException {
		Task task = new Task();
		User user = new User();
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		task.setTaskDatetimeStop(new Date());

		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestion(new Question());

		AnswersLog answersLog = new AnswersLog();
		answersLog.setAnswer(new Answer());
		questionLog.addAnswersLog(answersLog);
		task.addQuestionLog(questionLog);

		when(mockConnectionFactory.getConnection()).thenReturn(mockConnection);
		when(mockTaskDao.addOrUpdate(task, mockConnection)).thenReturn(task);
		when(mockQuestionLogService.addQuestionLog(questionLog, mockConnection)).thenReturn(questionLog);
		when(mockAnswersLogService.addAnswersLog(answersLog, mockConnection)).thenReturn(null);

		boolean res = taskService.addTaskWithQuestionsLogAndAnswersLog(task);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		verify(mockConnection, times(1)).rollback();
		verify(mockConnectionFactory, times(1)).getConnection();
		verify(mockTaskDao, times(1)).addOrUpdate(task, mockConnection);
		verify(mockQuestionLogService, times(1)).addQuestionLog(questionLog, mockConnection);
		verify(mockAnswersLogService, times(1)).addAnswersLog(answersLog, mockConnection);

		assertFalse(res);
	}

	@Test
	public void addTaskWithQuestionsLogAndAnswersLogFailNoQuestion() throws SQLException {
		Task task = new Task();
		User user = new User();
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		task.setTaskDatetimeStop(new Date());

		QuestionLog questionLog = initQuestionLog(0);
		task.addQuestionLog(questionLog);

		when(mockConnectionFactory.getConnection()).thenReturn(mockConnection);
		when(mockTaskDao.addOrUpdate(task, mockConnection)).thenReturn(task);
		when(mockQuestionLogService.addQuestionLog(questionLog, mockConnection)).thenReturn(null);

		boolean res = taskService.addTaskWithQuestionsLogAndAnswersLog(task);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		verify(mockConnection, times(1)).rollback();
		verify(mockConnectionFactory, times(1)).getConnection();
		verify(mockTaskDao, times(1)).addOrUpdate(task, mockConnection);
		verify(mockQuestionLogService, times(1)).addQuestionLog(questionLog, mockConnection);

		assertFalse(res);
	}

	@Test
	public void addTaskWithQuestionsLogAndAnswersLogFailNoTask() throws SQLException {
		Task task = initFullTask();

		when(mockConnectionFactory.getConnection()).thenReturn(mockConnection);
		when(mockTaskDao.addOrUpdate(task, mockConnection)).thenReturn(null);

		boolean res = taskService.addTaskWithQuestionsLogAndAnswersLog(task);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		verify(mockConnection, times(1)).rollback();
		verify(mockConnectionFactory, times(1)).getConnection();
		verify(mockTaskDao, times(1)).addOrUpdate(task, mockConnection);

		assertFalse(res);
	}

	@Test
	public void addTaskWithQuestionsLogAndAnswersLogFailSqlExeption() throws SQLException {
		Task task = initFullTask();

		when(mockConnectionFactory.getConnection()).thenReturn(mockConnection);
		doThrow(new SQLException()).when(mockConnection).setAutoCommit(false);

		boolean res = taskService.addTaskWithQuestionsLogAndAnswersLog(task);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		verify(mockConnectionFactory, times(1)).getConnection();

		assertFalse(res);
	}

	@Test
	public void addTaskWithQuestionsLogAndAnswersLogNullTask() throws SQLException {

		boolean res = taskService.addTaskWithQuestionsLogAndAnswersLog(null);

		verify(mockConnection, times(0)).setAutoCommit(anyBoolean());
		verify(mockConnection, times(0)).close();
		verify(mockConnection, times(0)).rollback();
		verify(mockConnectionFactory, times(0)).getConnection();
		verify(mockTaskDao, times(0)).addOrUpdate(anyObject(), anyObject());

		assertFalse(res);
	}

	@Test
	public void calculateTascScore() {
		Task task = initFullTask();

		int res = taskService.calculateTascScore(task);

		assertEquals(50, res);
	}

	@Test
	public void insertTasksToTestsListByUser() {
		Task task = new Task();
		User user = new User();
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		test.setTestId(1);
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		task.setTaskDatetimeStop(new Date());

		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestion(new Question());

		AnswersLog answersLog = new AnswersLog();
		answersLog.setAnswer(new Answer());
		questionLog.addAnswersLog(answersLog);
		task.addQuestionLog(questionLog);

		List<com.shpach.tutor.persistance.entities.Test> tests = new ArrayList<>(Arrays.asList(test));
		List<Task> tasks = new ArrayList<Task>(Arrays.asList(task));
		when(mockTaskDao.findTaskByTestIdAndUserId(tests.get(0).getTestId(), user.getUserId())).thenReturn(tasks);

		taskService.insertTasksToTestsListByUser(tests, user);

		verify(mockTaskDao, times(1)).findTaskByTestIdAndUserId(tests.get(0).getTestId(), user.getUserId());
		assertArrayEquals(tests.get(0).getTasks().toArray(), tasks.toArray());
	}

	@Test
	public void insertTasksToTestsListByUserNullTests() {

		taskService.insertTasksToTestsListByUser(null, new User());

		verify(mockTaskDao, times(0)).findTaskByTestIdAndUserId(anyInt(), anyInt());
		assertTrue(true);
	}

	@Test
	public void insertTasksToTestsListByUserNullUser() {
		List<com.shpach.tutor.persistance.entities.Test> tests = new ArrayList<>();

		taskService.insertTasksToTestsListByUser(tests, null);

		verify(mockTaskDao, times(0)).findTaskByTestIdAndUserId(anyInt(), anyInt());

	}

	@Test
	public void CalculateAverageScore() {
		Task task1 = new Task();
		task1.setTaskScore((byte) 90);
		Task task2 = new Task();
		task2.setTaskScore((byte) 40);
		Task task3 = new Task();
		task3.setTaskScore((byte) 30);
		List<Task> tasks = new ArrayList<Task>(Arrays.asList(task1, task2, task3));
		int actuals = taskService.CalculateAverageScore(tasks);

		assertEquals(53, actuals);
	}

	@Test
	public void CalculateAverageScoreNullTasks() {
		int actuals = taskService.CalculateAverageScore(null);

		assertEquals(-1, actuals);
	}

	@Test
	public void CalculateAverageScoreEmptyTasks() {
		List<Task> tasks = new ArrayList<Task>();
		int actuals = taskService.CalculateAverageScore(tasks);

		assertEquals(-1, actuals);
	}

	@Test
	public void insertTasksToTestsList() {
		Task task = new Task();
		User user = new User();
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		test.setTestId(1);
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		task.setTaskDatetimeStop(new Date());

		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestion(new Question());

		AnswersLog answersLog = new AnswersLog();
		answersLog.setAnswer(new Answer());
		questionLog.addAnswersLog(answersLog);
		task.addQuestionLog(questionLog);

		List<com.shpach.tutor.persistance.entities.Test> tests = new ArrayList<>(Arrays.asList(test));
		List<Task> tasks = new ArrayList<Task>(Arrays.asList(task));
		when(mockTaskDao.findTaskByTestId(tests.get(0).getTestId())).thenReturn(tasks);

		taskService.insertTasksToTestsList(tests);

		verify(mockTaskDao, times(1)).findTaskByTestId(tests.get(0).getTestId());
		assertArrayEquals(tests.get(0).getTasks().toArray(), tasks.toArray());
	}

	@Test
	public void insertTasksToTestsListNullTests() {

		taskService.insertTasksToTestsList(null);

		verify(mockTaskDao, times(0)).findTaskByTestId(anyInt());

	}

	@Test
	public void getTasksByUser() {
		Task task1 = new Task();
		task1.setTaskScore((byte) 90);
		Task task2 = new Task();
		task2.setTaskScore((byte) 40);
		Task task3 = new Task();
		task3.setTaskScore((byte) 30);

		User user = new User();
		user.setUserId(123);

		List<Task> expecteds = new ArrayList<Task>(Arrays.asList(task1, task2, task3));

		when(mockTaskDao.findTaskByUserId(user.getUserId())).thenReturn(expecteds);

		List<Task> actuals = taskService.getTasksByUser(user);

		verify(mockTaskDao, times(1)).findTaskByUserId(user.getUserId());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());

	}

	@Test
	public void getTasksByUserNullUser() {

		List<Task> actuals = taskService.getTasksByUser(null);

		verify(mockTaskDao, times(0)).findTaskByUserId(anyInt());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());

	}

	@Test
	public void getTasksByUserReturnNull() {

		User user = new User();
		user.setUserId(123);

		when(mockTaskDao.findTaskByUserId(user.getUserId())).thenReturn(null);

		List<Task> actuals = taskService.getTasksByUser(user);

		verify(mockTaskDao, times(1)).findTaskByUserId(user.getUserId());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());

	}

	@Test
	public void getMinScoreTest() {
		Task task1 = new Task();
		task1.setTaskScore((byte) 90);
		Task task2 = new Task();
		task2.setTaskScore((byte) 40);
		Task task3 = new Task();
		task3.setTaskScore((byte) 30);
		List<Task> tasks = new ArrayList<Task>(Arrays.asList(task1, task2, task3));
		int actuals = taskService.getMinScore(tasks);

		assertEquals(30, actuals);
	}

	@Test
	public void getMinScoreTestNull() {

		int actuals = taskService.getMinScore(null);

		assertEquals(Integer.MAX_VALUE, actuals);
	}

	@Test
	public void getHardestTestByTasksScore() {
		Task task1 = new Task();
		task1.setTaskScore((byte) 90);
		task1.setTestId(1);
		Task task2 = new Task();
		task2.setTaskScore((byte) 40);
		task2.setTestId(1);
		Task task3 = new Task();
		task3.setTaskScore((byte) 30);
		task3.setTestId(2);
		Task task4 = new Task();
		task4.setTaskScore((byte) 30);
		task4.setTestId(2);
		List<Task> tasks = new ArrayList<Task>(Arrays.asList(task1, task2, task3, task4));
		com.shpach.tutor.persistance.entities.Test expected = new com.shpach.tutor.persistance.entities.Test();
		expected.setTestId(2);
		when(mockTaskDao.findAllTasks()).thenReturn(tasks);
		when(mockTestService.getTestById(expected.getTestId())).thenReturn(expected);

		com.shpach.tutor.persistance.entities.Test actual = taskService.getHardestTestByTasksScore();

		verify(mockTaskDao, times(1)).findAllTasks();
		verify(mockTestService, times(1)).getTestById(expected.getTestId());

		assertEquals(expected, actual);
	}

	@Test
	public void getHardestTestByTasksScoreNull() {
		when(mockTaskDao.findAllTasks()).thenReturn(null);

		com.shpach.tutor.persistance.entities.Test actual = taskService.getHardestTestByTasksScore();

		verify(mockTaskDao, times(1)).findAllTasks();
		verify(mockTestService, times(0)).getTestById(anyInt());

		assertNull(actual);
	}

	private Task initFullTask() {
		Task task = new Task();
		User user = new User();
		com.shpach.tutor.persistance.entities.Test test = new com.shpach.tutor.persistance.entities.Test();
		task.setUser(user);
		task.setTest(test);
		task.setTaskDatetimeStart(new Date());
		task.setTaskDatetimeStop(new Date());

		QuestionLog questionLog = initQuestionLog(0);
		task.addQuestionLog(questionLog);
		questionLog = initQuestionLog(3);
		task.addQuestionLog(questionLog);
		return task;
	}

	private QuestionLog initQuestionLog(int param) {
		int[] values = { 2, 3, 2, 1, 4, 1 };
		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestion(new Question());
		for (int i = param; i < param + 3; i++) {
			AnswersLog answersLog = initAnswerLog(values[i]);
			questionLog.addAnswersLog(answersLog);

		}

		return questionLog;
	}

	private AnswersLog initAnswerLog(int param) {
		// 1 - answer not Correct, log not checked 2 - answer not Correct, log
		// checked 3 - answer Correct, log not checked 4 - answer Correct, log
		// checked
		AnswersLog answersLog = new AnswersLog();
		Answer answer = new Answer();
		if (param == 3 || param == 4)
			answer.setAnswerCorrect((byte) 1);
		answersLog.setAnswer(answer);

		if (param == 2 || param == 4)
			answersLog.setAnswerChecked((byte) 1);

		return answersLog;
	}
}
