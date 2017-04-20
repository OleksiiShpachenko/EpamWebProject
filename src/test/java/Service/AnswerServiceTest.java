package Service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.jdbc.dao.answer.IAnswerDao;
import com.shpach.tutor.service.AnswerService;

import TestUtils.TestUtils;

public class AnswerServiceTest {
	private AnswerService answerService;
	private IAnswerDao mockAnswerDao;

	@Before
	public void init() {
		answerService = AnswerService.getInstance();

		mockAnswerDao = Mockito.mock(IAnswerDao.class);

		TestUtils.getInstance().mockPrivateField(answerService, "answerDao", mockAnswerDao);
	}

	@Test
	public void addNewAnswersList() {
		Answer answer = new Answer();
		answer.setAnswerId(1);
		List<Answer> answerList = new ArrayList<>(Arrays.asList(answer));

		when(mockAnswerDao.addOrUpdate(answer)).thenReturn(answer);

		boolean actual = answerService.addNewAnswersList(answerList);

		verify(mockAnswerDao, times(1)).addOrUpdate(answer);

		assertTrue(actual);
	}

	@Test
	public void addNewAnswersListFail() {
		Answer answer = new Answer();
		answer.setAnswerId(1);
		List<Answer> answerList = new ArrayList<>(Arrays.asList(answer));

		when(mockAnswerDao.addOrUpdate(answer)).thenReturn(null);

		boolean actual = answerService.addNewAnswersList(answerList);

		verify(mockAnswerDao, times(1)).addOrUpdate(answer);

		assertFalse(actual);
	}

	@Test
	public void addNewAnswersListNull() {
		boolean actual = answerService.addNewAnswersList(null);

		verify(mockAnswerDao, times(0)).addOrUpdate(anyObject());

		assertFalse(actual);
	}
	
	@Test
	public void getAnswersByQuestion(){
		Question question=new Question();
		question.setQuestionId(1);
		Answer answer = new Answer();
		answer.setAnswerId(1);
		List<Answer> answerList = new ArrayList<>(Arrays.asList(answer));

		when(mockAnswerDao.findAnswerByQuestionId(question.getQuestionId())).thenReturn(answerList);
		
		List<Answer> actuals=answerService.getAnswersByQuestion(question);
		
		verify(mockAnswerDao,times(1)).findAnswerByQuestionId(question.getQuestionId());
		
		assertArrayEquals(answerList.toArray(), actuals.toArray());
	}
	@Test
	public void getAnswersByQuestionNullReturnAnswerList(){
		Question question=new Question();
		question.setQuestionId(1);

		when(mockAnswerDao.findAnswerByQuestionId(question.getQuestionId())).thenReturn(null);
		
		List<Answer> actuals=answerService.getAnswersByQuestion(question);
		
		verify(mockAnswerDao,times(1)).findAnswerByQuestionId(question.getQuestionId());
		
		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}
	@Test
	public void getAnswersByQuestionNullQuestion(){
	
		List<Answer> actuals=answerService.getAnswersByQuestion(null);
		
		verify(mockAnswerDao,times(0)).findAnswerByQuestionId(anyInt());
		
		assertNotNull(actuals);
		assertEquals(0, actuals.size());
	}
	@Test
	public void	getAnswerById(){
		Answer answer = new Answer();
		answer.setAnswerId(1);
		
		when(mockAnswerDao.findAnswerById(answer.getAnswerId())).thenReturn(answer);
		
		
		Answer actual=answerService.getAnswerById(answer.getAnswerId());
		verify(mockAnswerDao, times(1)).findAnswerById(answer.getAnswerId());
		
		assertEquals(answer, actual);
	}
	@Test
	public void	getAnswerByIdNotExist(){
		Answer answer = new Answer();
		answer.setAnswerId(1);
		
		when(mockAnswerDao.findAnswerById(answer.getAnswerId())).thenReturn(null);
				
		Answer actual=answerService.getAnswerById(answer.getAnswerId());
		
		verify(mockAnswerDao, times(1)).findAnswerById(answer.getAnswerId());
		
		assertNull(actual);
	}
}
