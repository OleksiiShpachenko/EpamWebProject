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

import com.shpach.tutor.persistance.entities.Category;
import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.jdbc.connection.ConnectionPool;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;

@PowerMockIgnore("javax.management.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(ConnectionPool.class)
public class CommunityDaoTest {
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
	public void findCommunityByIdTestExistId() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(communtyId);
		when(mockResultSet.getByte(anyInt())).thenReturn(communityActive);
		when(mockResultSet.getString(anyInt())).thenReturn(communityName, communityDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Community community= new Community();
		community.setCommunityId(communtyId);
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		Community communityExpected = communityDao.findCommunityById(communtyId);

		assertEquals(communityExpected, community);
	}

	@Test
	public void findCommunityByIdTestNotExistId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		Community communityExpected = communityDao.findCommunityById(2);

		assertNull(communityExpected);
	}

	@Test
	public void findCommunityByTestIdTestExistId() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(communtyId);
		when(mockResultSet.getByte(anyInt())).thenReturn(communityActive);
		when(mockResultSet.getString(anyInt())).thenReturn(communityName, communityDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Community community= new Community();
		community.setCommunityId(communtyId);
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		List<Community> communities = new ArrayList<>();
		communities.add(community);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communityExpected = communityDao.findCommunityByTestId(2);

		assertArrayEquals(communityExpected.toArray(), communities.toArray());
	}

	@Test
	public void findCommunityByTestIdTestNotExistId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communityExpected = communityDao.findCommunityByTestId(2);

		assertNull(communityExpected);
	}

	@Test
	public void findCommunityByUserIdTestExistId() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(communtyId);
		when(mockResultSet.getByte(anyInt())).thenReturn(communityActive);
		when(mockResultSet.getString(anyInt())).thenReturn(communityName, communityDescription);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

		Community community= new Community();
		community.setCommunityId(communtyId);
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		List<Community> communities = new ArrayList<>();
		communities.add(community);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communityExpected = communityDao.findCommunityByUserId(2);

		assertArrayEquals(communityExpected.toArray(), communities.toArray());
	}

	@Test
	public void findCommunityByUserIdTestNotExistId() throws SQLException, NamingException {

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.next()).thenReturn(Boolean.FALSE);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		List<Community> communityExpected = communityDao.findCommunityByUserId(2);
		
		assertNull(communityExpected);
	}

	@Test
	public void addOrApdateTestUpdateCommunity() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

		Community community= new Community();
		community.setCommunityId(communtyId);
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		Community communityExpected = communityDao.addOrUpdate(community);
		
		assertNotNull(communityExpected);
	}

	@Test
	public void addOrApdateTestNewCategory() throws SQLException, NamingException {
		int communtyId = 1;
		String communityDescription = "Community descr";
		String communityName = "Community name";
		byte communityActive = 1;
		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		when(mockResultSet.getInt(anyInt())).thenReturn(communtyId);
		when(mockResultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);
		
		Community community= new Community();
		community.setCommunityDescription(communityDescription);
		community.setCommunityName(communityName);
		community.setCommunityActive(communityActive);
		
		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		Community communityExpected = communityDao.addOrUpdate(community);

		assertEquals(communtyId, communityExpected.getCommunityId());
	}

	@Test
	public void assignUserToCommunityTestGoodParam() throws SQLException, NamingException {
		int userId = 1;
		int communityId = 2;

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		boolean res  = communityDao.assignUserToCommunity(userId, communityId);

		assertTrue(res);
	}

	@Test
	public void assignUserToCommunityTestWrongParam() throws SQLException, NamingException {
		int userId = 1;
		int communityId = 2;

		PowerMockito.mockStatic(ConnectionPool.class);
		PowerMockito.when(ConnectionPool.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeUpdate()).thenThrow(new SQLException());

		IDaoFactory daoFactory = new MySqlDaoFactory();
		ICommunityDao communityDao = daoFactory.getCommunityDao();
		boolean res  = communityDao.assignUserToCommunity(userId, communityId);

		assertFalse(res);
	}

}
