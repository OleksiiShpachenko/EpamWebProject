package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;
import com.shpach.tutor.persistance.jdbc.dao.question.MySqlQuestionDao;

public class QuestionDaoTest extends DaoTest {
	private IQuestionDao questionDao;
	private MySqlQuestionDao spyQuestionDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		questionDao = daoFactory.getQuestionDao();
		spyQuestionDao = (MySqlQuestionDao) spy(questionDao);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;

		Question questionExpected = new Question();
		questionExpected.setQuestionId(questionId);
		questionExpected.setQuestionText(questionText);
		questionExpected.setQuestionName(questionName);
		questionExpected.setUserId(userId);
		questionExpected.setQuestionActive(questionActive);

		when(mockResultSet.getInt(anyInt())).thenReturn(questionId, userId);
		when(mockResultSet.getByte(anyInt())).thenReturn(questionActive);
		when(mockResultSet.getString(anyInt())).thenReturn(questionText, questionName);

		Question questionActual = spyQuestionDao.populateDto(mockResultSet);

		verify(mockResultSet, times(2)).getInt(anyInt());
		verify(mockResultSet, times(2)).getString(anyInt());
		verify(mockResultSet, times(1)).getByte(anyInt());

		assertEquals(questionExpected, questionActual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Question questionActual = spyQuestionDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	@Test
	public void findAllTest() throws Exception {
		List<Question> expecteds = new ArrayList<>(Arrays.asList(new Question(), new Question()));

		doReturn(expecteds).when(spyQuestionDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Question> actuals = spyQuestionDao.findAll();

		verify(spyQuestionDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void addOrApdateTestNewQuestion() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;

		Question question = new Question();
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);

		Question questionExpected = new Question();
		questionExpected.setQuestionId(questionId);
		questionExpected.setQuestionText(questionText);
		questionExpected.setQuestionName(questionName);
		questionExpected.setUserId(userId);
		questionExpected.setQuestionActive(questionActive);

		doReturn(questionId).when(spyQuestionDao).dynamicAdd(anyString(), anyObject());

		Question questionActuals = spyQuestionDao.addOrUpdate(question);

		verify(spyQuestionDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(questionExpected, questionActuals);
	}

	@Test
	public void addOrApdateTestNewQuestionFail() throws SQLException, NamingException {
		// int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;

		Question question = new Question();
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);

		doReturn(0).when(spyQuestionDao).dynamicAdd(anyString(), anyObject());

		Question questionActuals = spyQuestionDao.addOrUpdate(question);

		verify(spyQuestionDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(questionActuals);
	}

	@Test
	public void addOrApdateTestUpdateQuestion() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;

		Question questionExpected = new Question();
		questionExpected.setQuestionId(questionId);
		questionExpected.setQuestionText(questionText);
		questionExpected.setQuestionName(questionName);
		questionExpected.setUserId(userId);
		questionExpected.setQuestionActive(questionActive);

		doReturn(true).when(spyQuestionDao).dynamicUpdate(anyString(), anyObject());

		Question questionActuals = spyQuestionDao.addOrUpdate(questionExpected);

		verify(spyQuestionDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(questionExpected, questionActuals);
	}

	@Test
	public void addOrApdateTestUpdateQuestionFail() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;

		Question questionExpected = new Question();
		questionExpected.setQuestionId(questionId);
		questionExpected.setQuestionText(questionText);
		questionExpected.setQuestionName(questionName);
		questionExpected.setUserId(userId);
		questionExpected.setQuestionActive(questionActive);

		doReturn(false).when(spyQuestionDao).dynamicUpdate(anyString(), anyObject());

		Question questionActuals = spyQuestionDao.addOrUpdate(questionExpected);

		verify(spyQuestionDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(questionActuals);
	}

	@Test
	public void findQuestionByIdTestExistId() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;
		Question question = new Question();
		question.setQuestionId(questionId);
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);

		doReturn(new ArrayList<Question>(Arrays.asList(question))).when(spyQuestionDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		Question questionActual = spyQuestionDao.findQuestionById(questionId);

		verify(spyQuestionDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(question, questionActual);
	}

	@Test
	public void findQuestionByIdTestNotExistId() throws SQLException, NamingException {
		int questionId = 1;

		doReturn(new ArrayList<Question>()).when(spyQuestionDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		Question questionActual = spyQuestionDao.findQuestionById(questionId);

		verify(spyQuestionDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(questionActual);

	}

	@Test
	public void findQuestionByUserIdTestExistUserId() throws SQLException, NamingException {
		int questionId = 1;
		int userId = 2;
		String questionText = "Question Text";
		String questionName = "Question name";
		byte questionActive = 1;

		Question question = new Question();
		question.setQuestionId(questionId);
		question.setQuestionText(questionText);
		question.setQuestionName(questionName);
		question.setUserId(userId);
		question.setQuestionActive(questionActive);
		List<Question> questions = new ArrayList<>();
		questions.add(question);

		doReturn(questions).when(spyQuestionDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Question> questionsActuals = spyQuestionDao.findQuestionByUserId(userId);

		verify(spyQuestionDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(questions.toArray(), questionsActuals.toArray());
	}

	public void findQuestionByUserIdTestNotExistUserId() throws SQLException, NamingException {
		int userId = 2;

		doReturn(new ArrayList<Question>()).when(spyQuestionDao).findByDynamicSelect(anyString(), anyString(),
				anyObject());

		List<Question> questionsActuals = spyQuestionDao.findQuestionByUserId(userId);

		verify(spyQuestionDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, questionsActuals.size());
	}

}
