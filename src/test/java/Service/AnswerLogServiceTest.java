package Service;

import org.junit.*;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.AnswersLog;
import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.jdbc.dao.answerlog.IAnswerLogDao;
import com.shpach.tutor.service.AnswerService;
import com.shpach.tutor.service.AnswersLogService;
import TestUtils.TestUtils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AnswerLogServiceTest {
	private AnswersLogService answersLogService;
	private IAnswerLogDao mockAnswersLogDao;
	private AnswerService mockAnswerService;

	@Before
	public void init() {
		answersLogService = AnswersLogService.getInstance();

		mockAnswersLogDao = Mockito.mock(IAnswerLogDao.class);
		mockAnswerService = Mockito.mock(AnswerService.class);

		TestUtils.getInstance().mockPrivateField(answersLogService, "answersLogDao", mockAnswersLogDao);
		TestUtils.getInstance().mockPrivateField(answersLogService, "answerService", mockAnswerService);
	}

	@Test
	public void addAnswersLog() {
		Connection mockConnection;
		mockConnection = Mockito.mock(Connection.class);
		AnswersLog answerLog = new AnswersLog();
		answerLog.setAnswerId(10);

		AnswersLog expectedAnswerLog = new AnswersLog();
		expectedAnswerLog.setAnswerId(10);
		expectedAnswerLog.setAnswerLogId(1);

		when(mockAnswersLogDao.addOrUpdate(answerLog, mockConnection)).thenReturn(expectedAnswerLog);

		AnswersLog actualsAnswerLog = answersLogService.addAnswersLog(answerLog, mockConnection);

		verify(mockAnswersLogDao, times(1)).addOrUpdate(answerLog, mockConnection);

		assertEquals(expectedAnswerLog, actualsAnswerLog);
	}

	@Test
	public void addAnswersLogNull() {
		Connection mockConnection;
		mockConnection = Mockito.mock(Connection.class);

		when(mockAnswersLogDao.addOrUpdate(null, mockConnection)).thenReturn(null);

		AnswersLog actualsAnswerLog = answersLogService.addAnswersLog(null, mockConnection);

		verify(mockAnswersLogDao, times(1)).addOrUpdate(null, mockConnection);

		assertNull(actualsAnswerLog);
	}

	@Test
	public void getAnsversLogsByQuestionLog() {
		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestionLogId(1);

		List<AnswersLog> expected = new ArrayList<>();
		AnswersLog answersLog = new AnswersLog();
		expected.add(answersLog);

		Answer answer = new Answer();
		answer.setAnswerId(1);

		when(mockAnswersLogDao.findAnswersLogByQuestionLogId(questionLog.getQuestionLogId())).thenReturn(expected);
		when(mockAnswerService.getAnswerById(expected.get(0).getAnswerId())).thenReturn(answer);

		List<AnswersLog> actuals = answersLogService.getAnsversLogsByQuestionLog(questionLog);

		verify(mockAnswersLogDao, times(1)).findAnswersLogByQuestionLogId(questionLog.getQuestionLogId());
		verify(mockAnswerService, times(1)).getAnswerById(expected.get(0).getAnswerId());

		assertArrayEquals(expected.toArray(), actuals.toArray());
		assertEquals(answer, expected.get(0).getAnswer());

	}

	@Test
	public void getAnsversLogsByQuestionLogNullAnswerLogs() {
		QuestionLog questionLog = new QuestionLog();
		questionLog.setQuestionLogId(1);

		when(mockAnswersLogDao.findAnswersLogByQuestionLogId(questionLog.getQuestionLogId())).thenReturn(null);

		List<AnswersLog> actuals = answersLogService.getAnsversLogsByQuestionLog(questionLog);

		verify(mockAnswersLogDao, times(1)).findAnswersLogByQuestionLogId(questionLog.getQuestionLogId());
		verify(mockAnswerService, times(0)).getAnswerById(anyInt());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}

	@Test
	public void getAnsversLogsByQuestionLogNullQuestionLog() {
		List<AnswersLog> actuals = answersLogService.getAnsversLogsByQuestionLog(null);

		verify(mockAnswersLogDao, times(0)).findAnswersLogByQuestionLogId(anyInt());
		verify(mockAnswerService, times(0)).getAnswerById(anyInt());

		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}
}
