package dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.naming.NamingException;

import org.junit.Before;
import org.junit.Test;

import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;
import com.shpach.tutor.persistance.jdbc.dao.test.MySqlTestDao;

public class TestDaoTest extends DaoTest {
	private ITestDao testDao;
	private MySqlTestDao spyTestDao;
	private int testId = 1;
	private int userId = 2;
	private byte testActive = 1;
	private byte testRndAnswer = 1;
	private int testType = 1;
	private byte testRndQuestion = 0;
	private String testName = "testName";
	private String testDescription = "TestDescr";

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		testDao = daoFactory.getTestDao();
		spyTestDao = (MySqlTestDao) spy(testDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<com.shpach.tutor.persistance.entities.Test> expecteds = new ArrayList<>(Arrays.asList(
				new com.shpach.tutor.persistance.entities.Test(), new com.shpach.tutor.persistance.entities.Test()));

		doReturn(expecteds).when(spyTestDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<com.shpach.tutor.persistance.entities.Test> actuals = spyTestDao.findAll();

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void addOrApdateTestNewAnswer() throws SQLException, NamingException {

		com.shpach.tutor.persistance.entities.Test inserted = initTestEntity();
		inserted.setTestId(0);

		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		doReturn(testId).when(spyTestDao).dynamicAdd(anyString(), anyObject());

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.addOrUpdate(inserted);

		verify(spyTestDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestNewAnswerFail() throws SQLException, NamingException {

		com.shpach.tutor.persistance.entities.Test inserted = initTestEntity();
		inserted.setTestId(0);

		doReturn(0).when(spyTestDao).dynamicAdd(anyString(), anyObject());

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.addOrUpdate(inserted);

		verify(spyTestDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestUpdateAnswer() throws SQLException, NamingException {
		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		doReturn(true).when(spyTestDao).dynamicUpdate(anyString(), anyObject());

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.addOrUpdate(expected);

		verify(spyTestDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestUpdateAnswerFail() throws SQLException, NamingException {
		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		doReturn(false).when(spyTestDao).dynamicUpdate(anyString(), anyObject());

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.addOrUpdate(expected);

		verify(spyTestDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findAnswerByIdTestExistId() throws SQLException, NamingException {
		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		doReturn(new ArrayList<com.shpach.tutor.persistance.entities.Test>(Arrays.asList(expected))).when(spyTestDao)
				.findByDynamicSelect(anyString(), anyString(), anyObject());

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.findTestById(testId);

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void findAnswerByIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<com.shpach.tutor.persistance.entities.Test>()).when(spyTestDao)
				.findByDynamicSelect(anyString(), anyString(), anyObject());

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.findTestById(testId);

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actual);

	}

	@Test
	public void findTestByCategoryIdOk() {
		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		List<com.shpach.tutor.persistance.entities.Test> expecteds = new ArrayList<>(Arrays.asList(expected));

		doReturn(expecteds).when(spyTestDao).findByDynamicSelect(anyString(), anyObject());

		List<com.shpach.tutor.persistance.entities.Test> actuals = spyTestDao.findTestByCategoryId(1);

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findTestByCategoryIdEmpty() {
		List<com.shpach.tutor.persistance.entities.Test> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyTestDao).findByDynamicSelect(anyString(), anyObject());

		List<com.shpach.tutor.persistance.entities.Test> actuals = spyTestDao.findTestByCategoryId(1);

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyObject());

		assertEquals(0, actuals.size());
	}

	@Test
	public void findTestByUserIdOk() {
		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		List<com.shpach.tutor.persistance.entities.Test> expecteds = new ArrayList<>(Arrays.asList(expected));

		doReturn(expecteds).when(spyTestDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<com.shpach.tutor.persistance.entities.Test> actuals = spyTestDao.findTestByUserId(1);

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void findTestByUserIdEmpty() {
		List<com.shpach.tutor.persistance.entities.Test> expecteds = new ArrayList<>();

		doReturn(expecteds).when(spyTestDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<com.shpach.tutor.persistance.entities.Test> actuals = spyTestDao.findTestByUserId(1);

		verify(spyTestDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(0, actuals.size());
	}
	
	@Test
	public void populateDtoTest() throws SQLException {
		com.shpach.tutor.persistance.entities.Test expected = initTestEntity();

		when(mockResultSet.getInt(anyInt())).thenReturn(testId, userId, testType);
		when(mockResultSet.getByte(anyInt())).thenReturn(testRndQuestion, testRndAnswer, testActive);
		when(mockResultSet.getString(anyInt())).thenReturn(testName, testDescription);

		com.shpach.tutor.persistance.entities.Test actual = spyTestDao.populateDto(mockResultSet);

		verify(mockResultSet, times(3)).getInt(anyInt());
		verify(mockResultSet, times(2)).getString(anyInt());
		verify(mockResultSet, times(3)).getByte(anyInt());

		assertEquals(expected, actual);
	}


	private com.shpach.tutor.persistance.entities.Test initTestEntity() {
		com.shpach.tutor.persistance.entities.Test inserted = new com.shpach.tutor.persistance.entities.Test();
		inserted.setTestId(testId);
		inserted.setUserId(userId);
		inserted.setTestName(testName);
		inserted.setTestDescription(testDescription);
		inserted.setTestRndQuestion(testRndQuestion);
		inserted.setTestRndAnswer(testRndAnswer);
		inserted.setTestType(testType);
		inserted.setTestActive(testActive);
		return inserted;
	}
}
