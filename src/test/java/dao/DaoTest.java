package dao;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mockito.Mockito;

import com.shpach.tutor.persistance.jdbc.connection.IConnectionPoolFactory;
import com.shpach.tutor.persistance.jdbc.dao.abstractdao.AbstractDao;

public class DaoTest {
	protected Connection mockConnection;
	protected PreparedStatement mockPreparedStmnt;
	protected ResultSet mockResultSet;
	protected IConnectionPoolFactory mockConnectionPoolFactory;
	public DaoTest() {
		super();
	}
	
	protected void init() throws SQLException {
		mockConnection = Mockito.mock(Connection.class);
		mockPreparedStmnt = Mockito.mock(PreparedStatement.class);
		mockResultSet = Mockito.mock(ResultSet.class);
		mockConnectionPoolFactory = Mockito.mock(IConnectionPoolFactory.class);
		when(mockConnectionPoolFactory.getConnection()).thenReturn(mockConnection);
		when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStmnt);
		when(mockPreparedStmnt.executeQuery()).thenReturn(mockResultSet);
		
	}

	protected void mockConnectionFactory(AbstractDao<?> abstractDao) {
		Field connectionFactoryField;
		try {
			connectionFactoryField = abstractDao.getClass().getSuperclass().getDeclaredField("connectionFactory");
			connectionFactoryField.setAccessible(true);
	        connectionFactoryField.set(abstractDao, mockConnectionPoolFactory);
		} catch (NoSuchFieldException | SecurityException|IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}