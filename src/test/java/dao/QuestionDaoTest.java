package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class QuestionDaoTest {
	private Connection mockConnection;
	private PreparedStatement mockPreparedStmnt;
	private ResultSet mockResultSet;

	@Before
	public void init() {
		mockConnection = Mockito.mock(Connection.class);
		mockPreparedStmnt = Mockito.mock(PreparedStatement.class);
		mockResultSet = Mockito.mock(ResultSet.class);
	}

	@Test
	public void findQuestionByIdTestExistId() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(questionId, userId);
		when(mockResultSet.getByte(anyInt())).thenReturn(questionActive);
		when(mockResultSet.getString(anyInt())).thenReturn(questionText, questionName);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		Question question = new Question();
		question.setQuestionId(questionId);
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		Question questionExpected = questionDao.findQuestionById(questionId);

		assertEquals(question, questionExpected);
	}

	@Test
	public void findQuestionByIdTestNotExistId() throws SQLException, NamingException {
		
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		Question questionExpected = questionDao.findQuestionById(2);

		assertNull(questionExpected);
	}

	@Test
	public void findQuestionByUserIdTestExistUserId() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(questionId, userId);
		when(mockResultSet.getByte(anyInt())).thenReturn(questionActive);
		when(mockResultSet.getString(anyInt())).thenReturn(questionText, questionName);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		Question question = new Question();
		question.setQuestionId(questionId);
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		List<Question> questionsExpected = questionDao.findQuestionByUserId(userId);

		assertArrayEquals(questions.toArray(), questionsExpected.toArray());
	}

	public void findQuestionByUserIdTestNotExistUserId() throws SQLException, NamingException {

		int userId = 2;

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		List<Question> questionsExpected = questionDao.findQuestionByUserId(userId);

		assertNull(questionsExpected);
	}

	@Test
	public void addOrApdateTestUpdateQuestion() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

		Question question = new Question();
		question.setQuestionId(questionId);
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		Question questionExpected = questionDao.addOrUpdate(question);

		assertNotNull(questionExpected);
	}

	@Test
	public void addOrApdateTestNewQuestion() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(questionId);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Question question = new Question();
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		Question questionExpected = questionDao.addOrUpdate(question);

		assertEquals(questionId, questionExpected.getQuestionId());
	}

}
