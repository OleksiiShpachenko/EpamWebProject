package dao;

import org.junit.*;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.jdbc.dao.answer.IAnswerDao;
import com.shpach.tutor.persistance.jdbc.dao.answer.MySqlAnswerDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

public class AnswerDaoTest extends DaoTest {
	private IAnswerDao answerDao;
	private MySqlAnswerDao spyAnswerDao;
	private int answerId = 1;
	private int questionId = 2;
	private int answerDefaultSortingOrder = 1;
	private String answerText = "Answer Text";
	private byte answerActive = 1;
	private byte answerCorrect = 0;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		answerDao = daoFactory.getAnswerDao();
		spyAnswerDao = (MySqlAnswerDao) spy(answerDao);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		Answer expected = initAnswer();

		when(mockResultSet.getInt(anyInt())).thenReturn(answerId, questionId, answerDefaultSortingOrder);
		when(mockResultSet.getByte(anyInt())).thenReturn(answerCorrect, answerActive);
		when(mockResultSet.getString(anyInt())).thenReturn(answerText);

		Answer actual = spyAnswerDao.populateDto(mockResultSet);

		verify(mockResultSet, times(3)).getInt(anyInt());
		verify(mockResultSet, times(1)).getString(anyInt());
		verify(mockResultSet, times(2)).getByte(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException {

		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		Answer actual = spyAnswerDao.populateDto(mockResultSet);

		verify(mockResultSet, times(1)).getInt(anyInt());

	}

	@Test
	public void findAllTest() throws Exception {
		List<Answer> expecteds = new ArrayList<>(Arrays.asList(new Answer(), new Answer()));

		doReturn(expecteds).when(spyAnswerDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Answer> actuals = spyAnswerDao.findAll();

		verify(spyAnswerDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void addOrApdateTestNewAnswer() throws SQLException, NamingException {

		Answer inserted = initAnswer();
		inserted.setAnswerId(0);

		Answer expected = initAnswer();

		doReturn(answerId).when(spyAnswerDao).dynamicAdd(anyString(), anyObject());

		Answer actual = spyAnswerDao.addOrUpdate(inserted);

		verify(spyAnswerDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestNewAnswerFail() throws SQLException, NamingException {

		Answer inserted = initAnswer();
		inserted.setAnswerId(0);

		doReturn(0).when(spyAnswerDao).dynamicAdd(anyString(), anyObject());

		Answer actual = spyAnswerDao.addOrUpdate(inserted);

		verify(spyAnswerDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestUpdateAnswer() throws SQLException, NamingException {
		Answer expected = initAnswer();

		doReturn(true).when(spyAnswerDao).dynamicUpdate(anyString(), anyObject());

		Answer actual = spyAnswerDao.addOrUpdate(expected);

		verify(spyAnswerDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestUpdateAnswerFail() throws SQLException, NamingException {
		Answer expected = initAnswer();

		doReturn(false).when(spyAnswerDao).dynamicUpdate(anyString(), anyObject());

		Answer actual = spyAnswerDao.addOrUpdate(expected);

		verify(spyAnswerDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findAnswerByIdTestExistId() throws SQLException, NamingException {
		Answer expected = initAnswer();

		doReturn(new ArrayList<Answer>(Arrays.asList(expected))).when(spyAnswerDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		Answer actual = spyAnswerDao.findAnswerById(answerId);

		verify(spyAnswerDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void findAnswerByIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<Answer>()).when(spyAnswerDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Answer actual = spyAnswerDao.findAnswerById(answerId);

		verify(spyAnswerDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);

	}

	@Test
	public void findAnswerByQuestionIdTestExistId() throws SQLException, NamingException {
		Answer expected = initAnswer();
		List<Answer> expecteds = new ArrayList<Answer>(Arrays.asList(expected));

		doReturn(expecteds).when(spyAnswerDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Answer> actuals = spyAnswerDao.findAnswerByQuestionId(questionId);

		verify(spyAnswerDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findAnswerByQuestionIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<Answer>()).when(spyAnswerDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Answer> actuals = spyAnswerDao.findAnswerByQuestionId(questionId);

		verify(spyAnswerDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());

	}

	private Answer initAnswer() {
		Answer expected = new Answer();
		expected.setAnswerId(answerId);
		expected.setQuestionId(questionId);
		expected.setAnswerText(answerText);
		expected.setAnswerActive(answerActive);
		expected.setAnswerCorrect(answerCorrect);
		expected.setAnswerDefaultSortingOrder(answerDefaultSortingOrder);
		return expected;
	}

}
