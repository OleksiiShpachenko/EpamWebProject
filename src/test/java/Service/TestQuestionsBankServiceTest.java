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

import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;
import com.shpach.tutor.service.QuestionService;
import com.shpach.tutor.service.TestQuestionsBankService;
import com.shpach.tutor.service.TestService;

import TestUtils.TestUtils;

public class TestQuestionsBankServiceTest {
	private TestQuestionsBankService testQuestionsBankService;
	private ITestQuestionsBankDao mockTestQuestionsBankDao;
	private TestService mockTestService;
	private QuestionService mockQuestionService;

	@Before
	public void init() {
		testQuestionsBankService = TestQuestionsBankService.getInstance();
		mockTestQuestionsBankDao = Mockito.mock(ITestQuestionsBankDao.class);
		mockTestService = Mockito.mock(TestService.class);
		mockQuestionService=Mockito.mock(QuestionService.class);
		
		TestUtils.getInstance().mockPrivateField(testQuestionsBankService, "testService", mockTestService);
		TestUtils.getInstance().mockPrivateField(testQuestionsBankService, "testQuestionsBankDao",
				mockTestQuestionsBankDao);
		TestUtils.getInstance().mockPrivateField(testQuestionsBankService, "questionService",
				mockQuestionService);
	}

	@Test
	public void getTestQuestionsBankWithTestInfoByQuestionTest() {
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
		TestQuestionsBank expectedTestQuestionBank = new TestQuestionsBank();
		expectedTestQuestionBank.setTest(expectedTests.get(0));
		List<TestQuestionsBank> expectedTestQuestionBankList = new ArrayList<>(Arrays.asList(expectedTestQuestionBank));
		TestQuestionsBank testQuestionBank = new TestQuestionsBank();
		testQuestionBank.setTestId(expectedTests.get(0).getTestId());

		when(mockTestQuestionsBankDao.findTestQuestionsBankByQuestionId(anyInt()))
				.thenReturn(new ArrayList<TestQuestionsBank>(Arrays.asList(testQuestionBank)));
		when(mockTestService.getTestById(expectedTests.get(0).getTestId())).thenReturn(expectedTests.get(0));

		List<TestQuestionsBank> testQuestionBanks = testQuestionsBankService
				.getTestQuestionsBankWithTestInfoByQuestion(new Question());

		verify(mockTestQuestionsBankDao, times(1)).findTestQuestionsBankByQuestionId(anyInt());
		verify(mockTestService, times(1)).getTestById(expectedTests.get(0).getTestId());

		assertArrayEquals(expectedTestQuestionBankList.toArray(), testQuestionBanks.toArray());
	}

	@Test
	public void getTestQuestionsBankWithTestInfoByQuestionTestNullQuestion() {

		List<TestQuestionsBank> testQuestionBanks = testQuestionsBankService
				.getTestQuestionsBankWithTestInfoByQuestion(null);

		verify(mockTestQuestionsBankDao, times(0)).findTestQuestionsBankByQuestionId(anyInt());
		verify(mockTestService, times(0)).getTestById(anyInt());

		assertNotNull(testQuestionBanks);
		assertEquals(0, testQuestionBanks.size());
	}

	@Test
	public void getTestQuestionsBankWithTestInfoByQuestionTestEnptyTestQuestionBank() {

		when(mockTestQuestionsBankDao.findTestQuestionsBankByQuestionId(anyInt()))
				.thenReturn(new ArrayList<TestQuestionsBank>());

		List<TestQuestionsBank> testQuestionBanks = testQuestionsBankService
				.getTestQuestionsBankWithTestInfoByQuestion(new Question());

		verify(mockTestQuestionsBankDao, times(1)).findTestQuestionsBankByQuestionId(anyInt());
		verify(mockTestService, times(0)).getTestById(anyInt());

		assertNotNull(testQuestionBanks);
		assertEquals(0, testQuestionBanks.size());
	}
	@Test
	public void getTestQuestionsBankWithTestInfoByQuestionTestNullTestQuestionBank() {

		when(mockTestQuestionsBankDao.findTestQuestionsBankByQuestionId(anyInt()))
				.thenReturn(null);

		List<TestQuestionsBank> testQuestionBanks = testQuestionsBankService
				.getTestQuestionsBankWithTestInfoByQuestion(new Question());

		verify(mockTestQuestionsBankDao, times(1)).findTestQuestionsBankByQuestionId(anyInt());
		verify(mockTestService, times(0)).getTestById(anyInt());

		assertNotNull(testQuestionBanks);
		assertEquals(0, testQuestionBanks.size());
	}
	
	@Test
	public void assignTestToQuestionStringValueCorrect(){
		int testId=1;
		int questionId=2;
		int questionDefaultSortingOrder=10;
		TestQuestionsBank testQuestionsBank = new TestQuestionsBank();
		testQuestionsBank.setTestId(testId);
		testQuestionsBank.setQuestionId(questionId);
		testQuestionsBank.setQuestionDefaultSortingOrder(questionDefaultSortingOrder);
		when(mockTestQuestionsBankDao.findMaxquestionDefaultSortingOrderByTestId(testId))
		.thenReturn(questionDefaultSortingOrder);
		when(mockTestQuestionsBankDao.addOrUpdate(anyObject()))
		.thenReturn(testQuestionsBank);
		
		boolean res=testQuestionsBankService.assignTestToQuestion(Integer.toString(testId), Integer.toString(questionId));
		
		verify(mockTestQuestionsBankDao, times(1)).findMaxquestionDefaultSortingOrderByTestId(testId);
		verify(mockTestQuestionsBankDao, times(1)).addOrUpdate(anyObject());
				
		assertTrue(res);
		
	}
	@Test
	public void assignTestToQuestionStringValueInCorrectTestId(){
		int questionId=2;
		boolean res=testQuestionsBankService.assignTestToQuestion("0", Integer.toString(questionId));
		
		verify(mockTestQuestionsBankDao, times(0)).findMaxquestionDefaultSortingOrderByTestId(anyInt());
		verify(mockTestQuestionsBankDao, times(0)).addOrUpdate(anyObject());
				
		assertFalse(res);
		
	}
	@Test
	public void assignTestToQuestionStringValueInCorrectQuestionId(){
	
		boolean res=testQuestionsBankService.assignTestToQuestion("1", "asd");
		
		verify(mockTestQuestionsBankDao, times(0)).findMaxquestionDefaultSortingOrderByTestId(anyInt());
		verify(mockTestQuestionsBankDao, times(0)).addOrUpdate(anyObject());
				
		assertFalse(res);
		
	}
	@Test
	public void getTestQuestionsBankWithQuestionsByTestId(){
		Question question=new Question();
		question.setQuestionId(2);
		List<com.shpach.tutor.persistance.entities.Test> expectedTests = TestUtils.getInstance().initTestsList();
		TestQuestionsBank expectedTestQuestionBank = new TestQuestionsBank();
		expectedTestQuestionBank.setTest(expectedTests.get(0));
		expectedTestQuestionBank.setQuestion(question);
		
		List<TestQuestionsBank> expectedTestQuestionBankList = new ArrayList<>(Arrays.asList(expectedTestQuestionBank));
		TestQuestionsBank testQuestionBank = new TestQuestionsBank();
		
		testQuestionBank.setTestId(expectedTests.get(0).getTestId());
		testQuestionBank.setQuestionId(question.getQuestionId());

		when(mockTestQuestionsBankDao.findTestQuestionsBankByTestId(anyInt()))
				.thenReturn(new ArrayList<TestQuestionsBank>(Arrays.asList(testQuestionBank)));
		when(mockQuestionService.getQuestionByIdWithAnswers(question.getQuestionId())).thenReturn(question);

		List<TestQuestionsBank> testQuestionBanks = testQuestionsBankService
				.getTestQuestionsBankWithQuestionsByTestId(expectedTests.get(0).getTestId());

		verify(mockTestQuestionsBankDao, times(1)).findTestQuestionsBankByTestId(anyInt());
		verify(mockQuestionService, times(1)).getQuestionByIdWithAnswers(question.getQuestionId());

		assertArrayEquals(expectedTestQuestionBankList.toArray(), testQuestionBanks.toArray());
		assertEquals(question, testQuestionBanks.get(0).getQuestion());
	}
	@Test
	public void getTestQuestionsBankWithQuestionsByTestIdNullResult(){

		when(mockTestQuestionsBankDao.findTestQuestionsBankByTestId(anyInt()))
				.thenReturn(null);

		List<TestQuestionsBank> testQuestionBanks = testQuestionsBankService
				.getTestQuestionsBankWithQuestionsByTestId(1);

		verify(mockTestQuestionsBankDao, times(1)).findTestQuestionsBankByTestId(anyInt());
		verify(mockTestService, times(0)).getTestById(anyInt());

		assertNotNull(testQuestionBanks);
		assertEquals(0,testQuestionBanks.size());
	}
}
