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

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.community.MySqlCommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

public class CommunityDaoTest extends DaoTest {
	private ICommunityDao communityDao;
	private MySqlCommunityDao spyCommunityDao;

	@Before
	public void init() throws SQLException {
		super.init();
		IDaoFactory daoFactory = new MySqlDaoFactory();
		communityDao = daoFactory.getCommunityDao();
		spyCommunityDao = (MySqlCommunityDao) spy(communityDao);
	}

	@Test
	public void findAllTest() throws Exception {
		List<Community> expecteds = new ArrayList<>(Arrays.asList(new Community(), new Community()));

		doReturn(expecteds).when(spyCommunityDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		List<Community> actuals = spyCommunityDao.findAll();

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertArrayEquals(expecteds.toArray(), actuals.toArray());
	}

	@Test
	public void addOrApdateTestUpdateCommunity() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community expected = new Community();
		expected.setCommunityId(communtyId);
		expected.setCommunityDescription(communityDescription);
		expected.setCommunityName(communityName);
		expected.setCommunityActive(communityActive);

		doReturn(true).when(spyCommunityDao).dynamicUpdate(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(expected);

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestUpdateCommunityFail() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community expected = new Community();
		expected.setCommunityId(communtyId);
		expected.setCommunityDescription(communityDescription);
		expected.setCommunityName(communityName);
		expected.setCommunityActive(communityActive);

		doReturn(false).when(spyCommunityDao).dynamicUpdate(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(expected);

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void addOrApdateTestNewCommunity() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community inserted = new Community();
		inserted.setCommunityDescription(communityDescription);
		inserted.setCommunityName(communityName);
		inserted.setCommunityActive(communityActive);

		Community expected = new Community();
		expected.setCommunityId(communtyId);
		expected.setCommunityDescription(communityDescription);
		expected.setCommunityName(communityName);
		expected.setCommunityActive(communityActive);

		doReturn(communtyId).when(spyCommunityDao).dynamicAdd(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(inserted);

		verify(spyCommunityDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertEquals(expected, actual);
	}

	@Test
	public void addOrApdateTestNewCommunityFail() throws SQLException, NamingException {
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community inserted = new Community();
		inserted.setCommunityDescription(communityDescription);
		inserted.setCommunityName(communityName);
		inserted.setCommunityActive(communityActive);

		doReturn(0).when(spyCommunityDao).dynamicAdd(anyString(), anyObject());

		Community actual = spyCommunityDao.addOrUpdate(inserted);

		verify(spyCommunityDao, times(1)).dynamicAdd(anyString(), anyObject());

		assertNull(actual);
	}

	@Test
	public void findCommunityByIdTestExistId() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community expected = new Community();
		expected.setCommunityId(communtyId);
		expected.setCommunityDescription(communityDescription);
		expected.setCommunityName(communityName);
		expected.setCommunityActive(communityActive);

		doReturn(new ArrayList<Community>(Arrays.asList(expected))).when(spyCommunityDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Community actuals = spyCommunityDao.findCommunityById(communtyId);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertEquals(expected, actuals);
	}

	@Test
	public void findCommunityByIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<Community>()).when(spyCommunityDao).findByDynamicSelect(anyString(), anyString(), anyObject());

		Community actuals = spyCommunityDao.findCommunityById(2);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyString(), anyObject());

		assertNull(actuals);
	}

	@Test
	public void findCommunityByCategoryIdTestExistId() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community community = new Community();
		community.setCommunityId(communtyId);
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		List<Community> expecteds = new ArrayList<>();
		expecteds.add(community);

		doReturn(expecteds).when(spyCommunityDao).findByDynamicSelect(anyString(), anyObject());

		List<Community> actuals = spyCommunityDao.findCommunityByCategoryId(2);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyObject());
		
		assertArrayEquals(actuals.toArray(), expecteds.toArray());
	}

	@Test
	public void findCommunityByCategoryIdTestNotExistId() throws SQLException, NamingException {

		doReturn(new ArrayList<Community>()).when(spyCommunityDao).findByDynamicSelect(anyString(),  anyObject());

		List<Community> actuals = spyCommunityDao.findCommunityByCategoryId(2);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(), anyObject());
		
		assertEquals(0, actuals.size());
	}

	@Test
	public void findCommunityByUserIdTestExistId() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community community = new Community();
		community.setCommunityId(communtyId);
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		List<Community> expecteds = new ArrayList<>();
		expecteds.add(community);

		doReturn(expecteds).when(spyCommunityDao).findByDynamicSelect(anyString(),  anyObject());

		List<Community> actuals = spyCommunityDao.findCommunityByUserId(2);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(),  anyObject());
		
		assertArrayEquals(actuals.toArray(), expecteds.toArray());
	}

	@Test
	public void findCommunityByUserIdTestNotExistId() throws SQLException, NamingException {
		
		doReturn(new ArrayList<Community>()).when(spyCommunityDao).findByDynamicSelect(anyString(),  anyObject());

		List<Community> actuals = spyCommunityDao.findCommunityByCategoryId(2);

		verify(spyCommunityDao, times(1)).findByDynamicSelect(anyString(),  anyObject());
		
		assertEquals(0, actuals.size());
	}

	@Test
	public void assignUserToCommunityTestGoodParam() throws SQLException, NamingException {
		int userId = 1;
		int communityId = 2;

		doReturn(true).when(spyCommunityDao).dynamicUpdate(anyString(),  anyObject());

		boolean res = spyCommunityDao.assignUserToCommunity(userId, communityId);;

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(),  anyObject());
		
		assertTrue(res);
	}

	@Test
	public void assignUserToCommunityTestWrongParam() throws SQLException, NamingException {
		int userId = 1;
		int communityId = 2;

		doReturn(false).when(spyCommunityDao).dynamicUpdate(anyString(),  anyObject());

		boolean res = spyCommunityDao.assignUserToCommunity(userId, communityId);;

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(),  anyObject());

		assertFalse(res);
	}
	@Test
	public void assignCategoryToCommunityTestGoodParam() throws SQLException, NamingException {
		int userId = 1;
		int communityId = 2;

		doReturn(true).when(spyCommunityDao).dynamicUpdate(anyString(),  anyObject());

		boolean res = spyCommunityDao.assignCategoryToCommunity(userId, communityId);;

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(),  anyObject());
		
		assertTrue(res);
	}

	@Test
	public void assignCategoryToCommunityTestWrongParam() throws SQLException, NamingException {
		int userId = 1;
		int communityId = 2;

		doReturn(false).when(spyCommunityDao).dynamicUpdate(anyString(),  anyObject());

		boolean res = spyCommunityDao.assignCategoryToCommunity(userId, communityId);;

		verify(spyCommunityDao, times(1)).dynamicUpdate(anyString(),  anyObject());

		assertFalse(res);
	}

	@Test
	public void populateDtoTest() throws SQLException{
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;

		Community expected = new Community();
		expected.setCommunityId(communtyId);
		expected.setCommunityDescription(communityDescription);
		expected.setCommunityName(communityName);
		expected.setCommunityActive(communityActive);
		
		when(mockResultSet.getInt(anyInt())).thenReturn(communtyId);
		when(mockResultSet.getString(anyInt())).thenReturn(communityName,communityDescription);
		when(mockResultSet.getByte(anyInt())).thenReturn(communityActive);
		
		Community actual= spyCommunityDao.populateDto(mockResultSet);
		
		verify(mockResultSet,times(1)).getInt(anyInt());
		verify(mockResultSet,times(2)).getString(anyInt());
		verify(mockResultSet,times(1)).getByte(anyInt());
		
		assertEquals(expected, actual);
	}
	@SuppressWarnings("unused")
	@Test(expected = SQLException.class)
	public void populateDtoTestException() throws SQLException{
	
		when(mockResultSet.getInt(anyInt())).thenThrow(new SQLException());
		
		Community actual= spyCommunityDao.populateDto(mockResultSet);
		
		verify(mockResultSet,times(1)).getInt(anyInt());
		
	}
}
