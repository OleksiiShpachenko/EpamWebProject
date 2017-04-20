package Service;
import org.junit.*;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.QuestionLog;
import com.shpach.tutor.persistance.jdbc.dao.questionlog.IQuestionLogDao;
import com.shpach.tutor.service.QuestionLogService;

import TestUtils.TestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
public class QuestionLogServiceTest {
	private QuestionLogService questionLogService;
	private IQuestionLogDao mockQuestionLogDao;
	@Before
	public void init() {
		questionLogService=QuestionLogService.getInstance();
		
		mockQuestionLogDao = Mockito.mock(IQuestionLogDao.class);
		
		TestUtils.getInstance().mockPrivateField(questionLogService, "questionLogDao", mockQuestionLogDao);
	}
	@Test
	public void addQuestionLog(){
		Connection mockConnection;
		mockConnection=Mockito.mock(Connection.class);
		QuestionLog questionLog =new QuestionLog(); 
		questionLog.setQuestionId(10);
		QuestionLog expectedQuestionLog=new QuestionLog();
		expectedQuestionLog.setQuestionId(10);
		expectedQuestionLog.setQuestionLogId(2);
		
		when(mockQuestionLogDao.addOrUpdate(questionLog, mockConnection)).thenReturn(expectedQuestionLog);
		
		QuestionLog actualsQuestionLog=questionLogService.addQuestionLog(questionLog, mockConnection);
		
		verify(mockQuestionLogDao, times(1)).addOrUpdate(questionLog, mockConnection);
		
		assertEquals(expectedQuestionLog, actualsQuestionLog);
	}
	@Test
	public void addQuestionLogNull(){
		Connection mockConnection;
		mockConnection=Mockito.mock(Connection.class);
	
		when(mockQuestionLogDao.addOrUpdate(null, mockConnection)).thenReturn(null);
		
		QuestionLog actualsQuestionLog=questionLogService.addQuestionLog(null, mockConnection);
		
		verify(mockQuestionLogDao, times(1)).addOrUpdate(null, mockConnection);
		
		assertNull(actualsQuestionLog);
	}
		
}
