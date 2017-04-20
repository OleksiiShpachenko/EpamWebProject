package dao;

import org.junit.*;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.List;

public class AbstractDaoTest extends DaoTest {
	private AbstractDao<Object> abstractDao;

	@SuppressWarnings("unchecked")
	@Before
	public void init() throws SQLException {
		super.init();
		abstractDao = (AbstractDao<Object>) Mockito.mock(AbstractDao.class);
		mockConnectionFactory(abstractDao);
		doCallRealMethod().when(abstractDao).getConnection();
	}

	@Test
	public void findByDynamicSelectOkOneItem() throws Exception {
		String sql = "select";
		String paramColumn = "paramColumn";
		Object paramValue = new Object();
		String sqlWithParam = sql + " WHERE " + paramColumn + "=?";
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());
		
		List<Object> obj = abstractDao.findByDynamicSelect(sql, paramColumn, paramValue);

		verify(mockConnection, times(1)).prepareStatement(sqlWithParam);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(1)).setObject(1, paramValue);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(1, obj.size());
	}

	@Test
	public void findByDynamicSelectOkNullItem() throws Exception {
		String sql = "select";
		String paramColumn = "paramColumn";
		Object paramValue = new Object();
		String sqlWithParam = sql + " WHERE " + paramColumn + "=?";
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());
		
		List<Object> obj = abstractDao.findByDynamicSelect(sql, paramColumn, paramValue);

		verify(mockConnection, times(1)).prepareStatement(sqlWithParam);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(1)).setObject(1, paramValue);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(1)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(0, obj.size());
	}

	@Test
	public void findByDynamicSelectExeptionPopulateDto() throws Exception {
		String sql = "select";
		String paramColumn = "paramColumn";
		Object paramValue = new Object();
		String sqlWithParam = sql + " WHERE " + paramColumn + "=?";
		when(abstractDao.populateDto(mockResultSet)).thenThrow(new SQLException());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());
		
		List<Object> obj = abstractDao.findByDynamicSelect(sql, paramColumn, paramValue);

		verify(mockConnection, times(1)).prepareStatement(sqlWithParam);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(1)).setObject(1, paramValue);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(1)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(0, obj.size());

	}

	@Test
	public void findByDynamicSelectFindAll() throws Exception {
		String sql = "select";
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());
		
		List<Object> obj = abstractDao.findByDynamicSelect(sql, null, null);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(0)).setObject(anyInt(), anyObject());
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(1, obj.size());
	}

	@Test
	public void findByDynamicSelectFindAllNullParamColumn() throws Exception {
		String sql = "select";
		Object paramValue = new Object();
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());

		List<Object> obj = abstractDao.findByDynamicSelect(sql, null, paramValue);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(0)).setObject(anyInt(), anyObject());
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(1, obj.size());
	}

	@Test
	public void findByDynamicSelectFindAllNullParamValue() throws Exception {
		String sql = "select";
		String paramColumn = "paramColumn";
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());

		List<Object> obj = abstractDao.findByDynamicSelect(sql, paramColumn, null);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(0)).setObject(anyInt(), anyObject());
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(1, obj.size());
	}

	@Test
	public void findByDynamicSelectExceptionConnection() throws Exception {
		String sql = "select";
		String paramColumn = "paramColumn";
		Object paramValue = new Object();
		String sqlWithParam = sql + " WHERE " + paramColumn + "=?";
		when(mockConnectionPoolFactory.getConnection()).thenThrow(new SQLException());
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());

		List<Object> obj = abstractDao.findByDynamicSelect(sql, paramColumn, paramValue);

		verify(mockConnection, times(0)).prepareStatement(sqlWithParam);
		verify(mockConnection, times(0)).close();
		verify(mockPreparedStmnt, times(0)).setObject(1, paramValue);
		verify(mockPreparedStmnt, times(0)).close();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).close();

		assertNotNull(obj);
		assertEquals(0, obj.size());

	}

	@Test
	public void findByDynamicSelectExceptionPrepareStatment() throws Exception {
		String sql = "select";
		String paramColumn = "paramColumn";
		Object paramValue = new Object();
		String sqlWithParam = sql + " WHERE " + paramColumn + "=?";

		doThrow(new SQLException()).when(mockPreparedStmnt).setObject(1, paramValue);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyString(), anyString());

		List<Object> obj = abstractDao.findByDynamicSelect(sql, paramColumn, paramValue);

		verify(mockConnection, times(1)).prepareStatement(sqlWithParam);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(1)).setObject(1, paramValue);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).close();

		assertNotNull(obj);
		assertEquals(0, obj.size());

	}

	@Test
	public void findByDynamicSelectArrayOk() throws Exception {
		String sql = "select";
		Object[] sqlParams = { new Object(), new Object() };
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyObject());

		List<Object> obj = abstractDao.findByDynamicSelect(sql, sqlParams);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(1)).setObject(1, sqlParams[0]);
		verify(mockPreparedStmnt, times(1)).setObject(2, sqlParams[1]);
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(1, obj.size());
	}

	@Test
	public void findByDynamicSelectArrayNull() throws Exception {
		String sql = "select";
		when(abstractDao.populateDto(mockResultSet)).thenReturn(new Object());
		when(mockResultSet.next()).thenReturn(true, false);
		doCallRealMethod().when(abstractDao).findByDynamicSelect(anyString(), anyObject());

		List<Object> obj = abstractDao.findByDynamicSelect(sql, null);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(1)).close();
		verify(mockPreparedStmnt, times(0)).setObject(anyInt(), anyObject());
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).close();

		assertNotNull(obj);
		assertEquals(1, obj.size());
	}

	@Test
	public void dynamicAdd() throws SQLException {
		String sql = "insert";
		Object[] sqlParams = { new Object(), new Object() };

		when(abstractDao.dynamicAdd(sql, mockConnection, sqlParams)).thenReturn(1);

		doCallRealMethod().when(abstractDao).dynamicAdd(anyString(), anyObject());

		int actual = abstractDao.dynamicAdd(sql, sqlParams);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		assertEquals(1, actual);
	}

	@Test
	public void dynamicAddExceprion() throws SQLException {
		String sql = "insert";
		Object[] sqlParams = { new Object(), new Object() };

		when(mockConnectionPoolFactory.getConnection()).thenThrow(new SQLException());

		doCallRealMethod().when(abstractDao).dynamicAdd(anyString(), anyObject());

		int actual = abstractDao.dynamicAdd(sql, sqlParams);

		verify(mockConnection, times(0)).setAutoCommit(false);
		verify(mockConnection, times(0)).setAutoCommit(true);
		verify(mockConnection, times(0)).close();
		assertEquals(0, actual);
	}

	@Test
	public void dynamicAddConn() throws SQLException {
		String sql = "insert";
		Object[] sqlParams = { new Object(), new Object() };

		when(mockResultSet.next()).thenReturn(true, false);
		when(mockResultSet.getInt(1)).thenReturn(1);

		doCallRealMethod().when(abstractDao).dynamicAdd(anyString(), anyObject(), anyObject());

		int actual = abstractDao.dynamicAdd(sql, mockConnection, sqlParams);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(1)).prepareStatement("SELECT last_insert_id()");
		verify(mockPreparedStmnt, times(1)).setObject(1, sqlParams[0]);
		verify(mockPreparedStmnt, times(1)).setObject(2, sqlParams[1]);
		verify(mockPreparedStmnt, times(1)).executeUpdate();
		verify(mockPreparedStmnt, times(2)).close();
		verify(mockPreparedStmnt, times(1)).executeQuery();
		verify(mockResultSet, times(2)).next();
		verify(mockResultSet, times(1)).getInt(1);
		verify(mockResultSet, times(1)).close();
		assertEquals(1, actual);
	}

	@Test
	public void dynamicAddConnNullParam() throws SQLException {
		String sql = "insert";

		doCallRealMethod().when(abstractDao).dynamicAdd(anyString(), anyObject(), anyObject());

		int actual = abstractDao.dynamicAdd(sql, mockConnection, null);

		verify(mockConnection, times(0)).prepareStatement(sql);
		verify(mockConnection, times(0)).prepareStatement("SELECT last_insert_id()");
		verify(mockPreparedStmnt, times(0)).setObject(anyInt(), anyObject());
		verify(mockPreparedStmnt, times(0)).executeUpdate();
		verify(mockPreparedStmnt, times(0)).close();
		verify(mockPreparedStmnt, times(0)).executeQuery();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).getInt(1);
		verify(mockResultSet, times(0)).close();
		assertEquals(0, actual);
	}

	@Test
	public void dynamicAddConnException() throws SQLException {
		String sql = "insert";
		Object[] sqlParams = { new Object(), new Object() };

		doThrow(new SQLException()).when(mockPreparedStmnt).executeUpdate();

		doCallRealMethod().when(abstractDao).dynamicAdd(anyString(), anyObject(), anyObject());

		int actual = abstractDao.dynamicAdd(sql, mockConnection, sqlParams);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockConnection, times(0)).prepareStatement("SELECT last_insert_id()");
		verify(mockPreparedStmnt, times(1)).setObject(1, sqlParams[0]);
		verify(mockPreparedStmnt, times(1)).setObject(2, sqlParams[1]);
		verify(mockPreparedStmnt, times(1)).executeUpdate();
		verify(mockPreparedStmnt, times(1)).close();
		verify(mockPreparedStmnt, times(0)).executeQuery();
		verify(mockResultSet, times(0)).next();
		verify(mockResultSet, times(0)).getInt(1);
		verify(mockResultSet, times(0)).close();
		assertEquals(0, actual);
	}

	@Test
	public void dynamicUpdate() throws SQLException {
		String sql = "update";
		Object[] sqlParams = { new Object(), new Object() };

		when(abstractDao.dynamicUpdate(sql, mockConnection, sqlParams)).thenReturn(true);

		doCallRealMethod().when(abstractDao).dynamicUpdate(anyString(), anyObject());

		boolean actual = abstractDao.dynamicUpdate(sql, sqlParams);

		verify(mockConnection, times(1)).setAutoCommit(false);
		verify(mockConnection, times(1)).setAutoCommit(true);
		verify(mockConnection, times(1)).close();
		assertTrue(actual);
	}

	@Test
	public void dynamicUpdateExceprion() throws SQLException {
		String sql = "update";
		Object[] sqlParams = { new Object(), new Object() };

		when(mockConnectionPoolFactory.getConnection()).thenThrow(new SQLException());

		doCallRealMethod().when(abstractDao).dynamicUpdate(anyString(), anyObject());

		boolean actual = abstractDao.dynamicUpdate(sql, sqlParams);

		verify(mockConnection, times(0)).setAutoCommit(false);
		verify(mockConnection, times(0)).setAutoCommit(true);
		verify(mockConnection, times(0)).close();
		assertFalse(actual);
	}

	@Test
	public void dynamicUpdateConn() throws SQLException {
		String sql = "update";
		Object[] sqlParams = { new Object(), new Object() };

		doCallRealMethod().when(abstractDao).dynamicUpdate(anyString(), anyObject(), anyObject());

		boolean actual = abstractDao.dynamicUpdate(sql, mockConnection, sqlParams);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockPreparedStmnt, times(1)).setObject(1, sqlParams[0]);
		verify(mockPreparedStmnt, times(1)).setObject(2, sqlParams[1]);
		verify(mockPreparedStmnt, times(1)).executeUpdate();
		verify(mockPreparedStmnt, times(1)).close();
		assertTrue(actual);
	}

	@Test
	public void dynamicUpdateConnNullParam() throws SQLException {
		String sql = "update";

		doCallRealMethod().when(abstractDao).dynamicUpdate(anyString(), anyObject(), anyObject());

		boolean actual = abstractDao.dynamicUpdate(sql, mockConnection, null);

		verify(mockConnection, times(0)).prepareStatement(sql);
		verify(mockPreparedStmnt, times(0)).setObject(anyInt(), anyObject());
		verify(mockPreparedStmnt, times(0)).executeUpdate();
		verify(mockPreparedStmnt, times(0)).close();
		assertFalse(actual);
	}

	@Test
	public void dynamicUpdateConnException() throws SQLException {
		String sql = "update";
		Object[] sqlParams = { new Object(), new Object() };

		doThrow(new SQLException()).when(mockPreparedStmnt).executeUpdate();

		doCallRealMethod().when(abstractDao).dynamicUpdate(anyString(), anyObject(), anyObject());

		boolean actual = abstractDao.dynamicUpdate(sql, mockConnection, sqlParams);

		verify(mockConnection, times(1)).prepareStatement(sql);
		verify(mockPreparedStmnt, times(1)).setObject(1, sqlParams[0]);
		verify(mockPreparedStmnt, times(1)).setObject(2, sqlParams[1]);
		verify(mockPreparedStmnt, times(1)).executeUpdate();
		verify(mockPreparedStmnt, times(1)).close();
		assertFalse(actual);
	}
}
