package dao;

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

import org.junit.Before;
import org.junit.Test;

import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.MySqlTestQuestionsBankDao;

public class TestQuestionsBankDaoTest extends DaoTest {
	private ITestQuestionsBankDao testQuestionsBankDao;
	private MySqlTestQuestionsBankDao spyTestQuestionsBankDao;
	private int testQuestionsBankId = 1;
	private int testId = 2;
	private int questionId = 3;
	private int questionDefaultSortingOrder = 4;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		testQuestionsBankDao = daoFactory.getTestQuestionsBankDao();
		spyTestQuestionsBankDao = (MySqlTestQuestionsBankDao) spy(testQuestionsBankDao);
	}

	@Test
	public void populateDtoTest() throws SQLException {
		TestQuestionsBank expected = initTestQuestionBank();

		when(mockResultSet.getInt(anyInt())).thenReturn(testQuestionsBankId, testId, questionId,
				questionDefaultSortingOrder);

		TestQuestionsBank actual = spyTestQuestionsBankDao.populateDto(mockResultSet);

		verify(mockResultSet, times(4)).getInt(anyInt());

		assertEquals(expected, actual);
	}

	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestFail() throws SQLException {
		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());

		TestQuestionsBank actual = spyTestQuestionsBankDao.populateDto(mockResultSet);

		verify(mockResultSet, times(4)).getInt(anyInt());

	}
	
	@Test
	public void findAllTest() throws Exception {
		List<TestQuestionsBank> expecteds = new ArrayList<>(Arrays.asList(new TestQuestionsBank(), new TestQuestionsBank()));

		doReturn(expecteds).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<TestQuestionsBank> actuals = spyTestQuestionsBankDao.findAll();

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void addOrApdateTestQuestionsBankUpdate() throws SQLException, NamingException {
		TestQuestionsBank expected = initTestQuestionBank();

		doReturn(true).when(spyTestQuestionsBankDao).dynamicUpdate(anyString(), anyObject());

		TestQuestionsBank actual = spyTestQuestionsBankDao.addOrUpdate(expected);

		verify(spyTestQuestionsBankDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestQuestionsBankUpdateFail() throws SQLException, NamingException {
		TestQuestionsBank expected = initTestQuestionBank();

		doReturn(false).when(spyTestQuestionsBankDao).dynamicUpdate(anyString(), anyObject());

		TestQuestionsBank actual = spyTestQuestionsBankDao.addOrUpdate(expected);

		verify(spyTestQuestionsBankDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestQuestionsBankNew() throws SQLException, NamingException {

		TestQuestionsBank inserted = initTestQuestionBank();
		inserted.setTestQuestionsBankId(0);

		TestQuestionsBank expected = initTestQuestionBank();

		doReturn(testQuestionsBankId).when(spyTestQuestionsBankDao).dynamicAdd(anyString(), anyObject());

		TestQuestionsBank actual = spyTestQuestionsBankDao.addOrUpdate(inserted);

		verify(spyTestQuestionsBankDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestQuestionsBankNewFail() throws SQLException, NamingException {

		TestQuestionsBank inserted = initTestQuestionBank();
		inserted.setTestQuestionsBankId(0);

		doReturn(0).when(spyTestQuestionsBankDao).dynamicAdd(anyString(), anyObject());

		TestQuestionsBank actual = spyTestQuestionsBankDao.addOrUpdate(inserted);

		verify(spyTestQuestionsBankDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}
	
	@Test
	public void findTestQuestionsBankByIdTestExistId() throws SQLException, NamingException {
		TestQuestionsBank expected = initTestQuestionBank();

		doReturn(new ArrayList<TestQuestionsBank>(Arrays.asList(expected))).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(),
				anyString(), anyObject());

		TestQuestionsBank actual = spyTestQuestionsBankDao.findTestQuestionsBankById(testQuestionsBankId);

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void findTestQuestionsBankByIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<TestQuestionsBank>()).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		TestQuestionsBank actual = spyTestQuestionsBankDao.findTestQuestionsBankById(testQuestionsBankId);

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);

	}

	@Test
	public void findTestQuestionsBankByTestIdTestExistId() throws SQLException, NamingException {
		TestQuestionsBank expected = initTestQuestionBank();
		List<TestQuestionsBank> expecteds = new ArrayList<>(Arrays.asList(expected));

		doReturn(expecteds).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<TestQuestionsBank> actuals = spyTestQuestionsBankDao.findTestQuestionsBankByTestId(testId);

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds, actuals);
	}

	@Test
	public void findTestQuestionsBankByTestIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<TestQuestionsBank>()).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<TestQuestionsBank> actuals = spyTestQuestionsBankDao.findTestQuestionsBankByTestId(testId);

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());

	}
	@Test
	public void findTestQuestionsBankByQuestionIdTestExistId() throws SQLException, NamingException {
		TestQuestionsBank expected = initTestQuestionBank();
		List<TestQuestionsBank> expecteds = new ArrayList<>(Arrays.asList(expected));

		doReturn(expecteds).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<TestQuestionsBank> actuals = spyTestQuestionsBankDao.findTestQuestionsBankByQuestionId(questionId);

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expecteds, actuals);
	}

	@Test
	public void findTestQuestionsBankByQuestionIdTestNotExistId() throws SQLException, NamingException {
		doReturn(new ArrayList<TestQuestionsBank>()).when(spyTestQuestionsBankDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<TestQuestionsBank> actuals = spyTestQuestionsBankDao.findTestQuestionsBankByQuestionId(questionId);

		verify(spyTestQuestionsBankDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());

	}
	
	@Test
	public void findMaxquestionDefaultSortingOrderByTestIdOk() throws SQLException{
		int testId=1;
		int expected=10;
		doReturn(mockConnection).when(spyTestQuestionsBankDao).getConnection();
		when(mockResultSet.next()).thenReturn(true, false);
		when(mockResultSet.getInt(1)).thenReturn(expected);
		
		int actual=spyTestQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId);
		
		verify(mockConnection, times(1)).prepareStatement(anyString());
		verify(mockPreparedStmnt, times(1)).setInt(1, testId);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockPreparedStmnt, times(1)).executeQuery();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).getInt(1);
		verify(mockResultSet, times(1)).close();
		verify(mockConnection, times(1)).close();
		
		assertEquals(expected, actual);
	}
	@Test
	public void findMaxquestionDefaultSortingOrderByTestIdConnException() throws SQLException{
		int testId=1;
		int expected=0;
		doThrow(new SQLException()).when(spyTestQuestionsBankDao).getConnection();
		
		int actual=spyTestQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId);
		
		verify(mockConnection, times(0)).prepareStatement(anyString());
		verify(mockPreparedStmnt, times(0)).setInt(1, testId);
		verify(mockPreparedStmnt, times(0)).close();
		verify(mockPreparedStmnt, times(0)).executeQuery();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).getInt(1);
		verify(mockResultSet, times(0)).close();
		verify(mockConnection, times(0)).close();
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void findMaxquestionDefaultSortingOrderByTestIdPrepareStatmentException() throws SQLException{
		int testId=1;
		int expected=0;
		doReturn(mockConnection).when(spyTestQuestionsBankDao).getConnection();
		when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());
		
		int actual=spyTestQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId);
		
		verify(mockConnection, times(1)).prepareStatement(anyString());
		verify(mockPreparedStmnt, times(0)).setInt(1, testId);
		verify(mockPreparedStmnt, times(0)).close();
		verify(mockPreparedStmnt, times(0)).executeQuery();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).getInt(1);
		verify(mockResultSet, times(0)).close();
		verify(mockConnection, times(1)).close();
		
		assertEquals(expected, actual);
	}
	@Test
	public void findMaxquestionDefaultSortingOrderByTestIdExecuteQueryException() throws SQLException{
		int testId=1;
		int expected=0;
		doReturn(mockConnection).when(spyTestQuestionsBankDao).getConnection();
		when(mockPreparedStmnt.executeQuery()).thenThrow(new SQLException());
		
		int actual=spyTestQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId);
		
		verify(mockConnection, times(1)).prepareStatement(anyString());
		verify(mockPreparedStmnt, times(1)).setInt(1, testId);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockPreparedStmnt, times(1)).executeQuery();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).getInt(1);
		verify(mockResultSet, times(0)).close();
		verify(mockConnection, times(1)).close();
		
		assertEquals(expected, actual);
	}
	@Test
	public void findMaxquestionDefaultSortingOrderByTestIdEmptyResult() throws SQLException{
		int testId=1;
		int expected=0;
		doReturn(mockConnection).when(spyTestQuestionsBankDao).getConnection();
		when(mockResultSet.next()).thenReturn(false);
		
		int actual=spyTestQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId);
		
		verify(mockConnection, times(1)).prepareStatement(anyString());
		verify(mockPreparedStmnt, times(1)).setInt(1, testId);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockPreparedStmnt, times(1)).executeQuery();
		verify(mockResultSet, times(1)).next();
		verify(mockResultSet, times(0)).getInt(1);
		verify(mockResultSet, times(1)).close();
		verify(mockConnection, times(1)).close();
		
		assertEquals(expected, actual);
	}
	private TestQuestionsBank initTestQuestionBank() {
		TestQuestionsBank expected = new TestQuestionsBank();
		expected.setTestQuestionsBankId(testQuestionsBankId);
		expected.setTestId(testId);
		expected.setQuestionId(questionId);
		expected.setQuestionDefaultSortingOrder(questionDefaultSortingOrder);
		return expected;
	}

}
