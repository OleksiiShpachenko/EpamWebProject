package Service;

import org.junit.*;
import org.mockito.Mockito;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;
import com.shpach.tutor.service.AnswerService;
import com.shpach.tutor.service.QuestionService;
import com.shpach.tutor.service.TestQuestionsBankService;

import TestUtils.TestUtils;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionServiceTest {
	private static QuestionService questionService;
	private IQuestionDao mockQuestionDao;
	private AnswerService mockAnswerService;
	private TestQuestionsBankService mockTestQuestionsBankService;

	@Before
	public void init() {
		questionService = QuestionService.getInstance();

		mockQuestionDao = Mockito.mock(IQuestionDao.class);
		mockAnswerService = Mockito.mock(AnswerService.class);
		mockTestQuestionsBankService = Mockito.mock(TestQuestionsBankService.class);

		TestUtils.getInstance().mockPrivateField(questionService, "questionDao", mockQuestionDao);
		TestUtils.getInstance().mockPrivateField(questionService, "answerService", mockAnswerService);
		TestUtils.getInstance().mockPrivateField(questionService, "testQuestionsBankService",
				mockTestQuestionsBankService);
	}

	@Test
	public void getQuestionsCountByUserOk() {
		User user = new User();
		user.setUserId(1);
		when(mockQuestionDao.findQuestionByUserId(user.getUserId())).thenReturn(
				new ArrayList<Question>(Arrays.asList(new Question(), new Question(), new Question(), new Question())));

		int actual = questionService.getQuestionsCountByUser(user);

		verify(mockQuestionDao, times(1)).findQuestionByUserId(user.getUserId());

		assertEquals(4, actual);
	}

	@Test
	public void getQuestionsCountByUserEmptyList() {
		User user = new User();
		user.setUserId(1);
		when(mockQuestionDao.findQuestionByUserId(user.getUserId())).thenReturn(new ArrayList<Question>());

		int actual = questionService.getQuestionsCountByUser(user);

		verify(mockQuestionDao, times(1)).findQuestionByUserId(user.getUserId());

		assertEquals(0, actual);
	}

	@Test
	public void getQuestionsCountByUserNullList() {
		User user = new User();
		user.setUserId(1);
		when(mockQuestionDao.findQuestionByUserId(user.getUserId())).thenReturn(null);

		int actual = questionService.getQuestionsCountByUser(user);

		verify(mockQuestionDao, times(1)).findQuestionByUserId(user.getUserId());

		assertEquals(0, actual);
	}

	@Test
	public void getQuestionsCountByUserNullUser() {
		User user = new User();
		user.setUserId(1);
		when(mockQuestionDao.findQuestionByUserId(user.getUserId())).thenReturn(new ArrayList<Question>());

		int actual = questionService.getQuestionsCountByUser(null);

		verify(mockQuestionDao, times(0)).findQuestionByUserId(user.getUserId());
		assertEquals(0, actual);
	}

	@Test
	public void getQuestionsByUserWithAnswersAndTestsList() {
		List<Question> questions = initQuestionList();
		List<Question> expectedQuestions = initQuestionList();

		User user = new User();
		user.setUserId(1);

		when(mockQuestionDao.findQuestionByUserId(user.getUserId())).thenReturn(new ArrayList<Question>(questions));
		when(mockAnswerService.getAnswersByQuestion(anyObject())).thenReturn(questions.get(0).getAnswers())
				.thenReturn(questions.get(1).getAnswers());
		when(mockTestQuestionsBankService.getTestQuestionsBankWithTestInfoByQuestion(anyObject()))
				.thenReturn(questions.get(0).getTestQuestionsBanks())
				.thenReturn(questions.get(1).getTestQuestionsBanks());

		List<Question> actual = questionService.getQuestionsByUserWithAnswersAndTestsList(user);

		verify(mockQuestionDao, times(1)).findQuestionByUserId(user.getUserId());
		verify(mockAnswerService, times(2)).getAnswersByQuestion(anyObject());
		verify(mockTestQuestionsBankService, times(2)).getTestQuestionsBankWithTestInfoByQuestion(anyObject());

		assertArrayEquals(expectedQuestions.toArray(), actual.toArray());
		assertArrayEquals(expectedQuestions.get(0).getAnswers().toArray(), actual.get(0).getAnswers().toArray());
		assertArrayEquals(expectedQuestions.get(1).getAnswers().toArray(), actual.get(1).getAnswers().toArray());
		assertArrayEquals(expectedQuestions.get(0).getTestQuestionsBanks().toArray(),
				actual.get(0).getTestQuestionsBanks().toArray());
		assertArrayEquals(expectedQuestions.get(1).getTestQuestionsBanks().toArray(),
				actual.get(1).getTestQuestionsBanks().toArray());

	}

	@Test
	public void getQuestionsByUserWithAnswersAndTestsListNullQuestions() {
		User user = new User();
		user.setUserId(1);

		when(mockQuestionDao.findQuestionByUserId(user.getUserId())).thenReturn(null);

		List<Question> actual = questionService.getQuestionsByUserWithAnswersAndTestsList(user);

		verify(mockQuestionDao, times(1)).findQuestionByUserId(user.getUserId());
		verify(mockAnswerService, times(0)).getAnswersByQuestion(anyObject());
		verify(mockTestQuestionsBankService, times(0)).getTestQuestionsBankWithTestInfoByQuestion(anyObject());

		assertNotNull(actual);
		assertEquals(0, actual.size());
	}

	@Test
	public void getQuestionsByUserWithAnswersAndTestsListNullUser() {

		List<Question> actual = questionService.getQuestionsByUserWithAnswersAndTestsList(null);

		verify(mockQuestionDao, times(0)).findQuestionByUserId(anyInt());
		verify(mockAnswerService, times(0)).getAnswersByQuestion(anyObject());
		verify(mockTestQuestionsBankService, times(0)).getTestQuestionsBankWithTestInfoByQuestion(anyObject());

		assertNotNull(actual);
		assertEquals(0, actual.size());
	}

	@Test
	public void addNewQuestion() {
		User user = new User();
		user.setUserId(1);

		Question expectedQuestion = new Question();
		expectedQuestion.setQuestionName("questionName");
		expectedQuestion.setQuestionText("questionText");
		expectedQuestion.setUserId(user.getUserId());
		expectedQuestion.setQuestionActive((byte) 1);
		List<Answer> answers = createAnswers();
		expectedQuestion.setAnswers(answers);

		Question addedQuestion = new Question();
		addedQuestion.setQuestionId(1);

		when(mockQuestionDao.addOrUpdate(expectedQuestion)).thenReturn(addedQuestion);
		when(mockAnswerService.addNewAnswersList(answers)).thenReturn(true);

		boolean actual = questionService.addNewQuestion("questionName", "questionText",
				new String[] { "answ1", "answ2", "answ3", "" }, new String[] { "0" }, user);

		verify(mockQuestionDao, times(1)).addOrUpdate(anyObject());
		verify(mockAnswerService, times(1)).addNewAnswersList(anyObject());

		assertTrue(actual);

	}

	@Test
	public void addNewQuestionFailValidation() {
		User user = new User();
		user.setUserId(1);

		boolean actual = questionService.addNewQuestion("questionName", "questionText",
				new String[] { "answ1", "answ2", "answ3", "" }, new String[] { "3" }, user);

		verify(mockQuestionDao, times(0)).addOrUpdate(anyObject());
		verify(mockAnswerService, times(0)).addNewAnswersList(anyObject());

		assertFalse(actual);

	}

	@Test
	public void addNewQuestionFailValidationNoName() {
		User user = new User();
		user.setUserId(1);

		boolean actual = questionService.addNewQuestion("", "questionText",
				new String[] { "answ1", "answ2", "answ3", "" }, new String[] { "3" }, user);

		verify(mockQuestionDao, times(0)).addOrUpdate(anyObject());
		verify(mockAnswerService, times(0)).addNewAnswersList(anyObject());

		assertFalse(actual);

	}

	@Test
	public void addNewQuestionFailValidationNoText() {
		User user = new User();
		user.setUserId(1);

		boolean actual = questionService.addNewQuestion("questionName", "",
				new String[] { "answ1", "answ2", "answ3", "" }, new String[] { "3" }, user);

		verify(mockQuestionDao, times(0)).addOrUpdate(anyObject());
		verify(mockAnswerService, times(0)).addNewAnswersList(anyObject());

		assertFalse(actual);

	}

	@Test
	public void addNewQuestionFailValidationNotEnouthAnswers() {
		User user = new User();
		user.setUserId(1);

		boolean actual = questionService.addNewQuestion("questionName", "questionText", new String[] { "answ1" },
				new String[] { "0" }, user);

		verify(mockQuestionDao, times(0)).addOrUpdate(anyObject());
		verify(mockAnswerService, times(0)).addNewAnswersList(anyObject());

		assertFalse(actual);

	}

	@Test
	public void addNewQuestionFailValidationNullUser() {
		User user = new User();
		user.setUserId(1);

		boolean actual = questionService.addNewQuestion("questionName", "questionText",
				new String[] { "answ1", "answ2" }, new String[] { "0" }, null);

		verify(mockQuestionDao, times(0)).addOrUpdate(anyObject());
		verify(mockAnswerService, times(0)).addNewAnswersList(anyObject());

		assertFalse(actual);

	}

	@Test
	public void getQuestionByIdWithAnswers() {
		List<Question> questions = initQuestionList();
		questions.get(0).setAnswers(null);
		List<Question> expectedQuestions = initQuestionList();

		when(mockQuestionDao.findQuestionById(questions.get(0).getQuestionId())).thenReturn(questions.get(0));
		when(mockAnswerService.getAnswersByQuestion(questions.get(0)))
				.thenReturn(expectedQuestions.get(0).getAnswers());

		Question actual = questionService.getQuestionByIdWithAnswers((questions.get(0).getQuestionId()));

		verify(mockQuestionDao, times(1)).findQuestionById(questions.get(0).getQuestionId());
		verify(mockAnswerService, times(1)).getAnswersByQuestion(questions.get(0));

		assertEquals(expectedQuestions.get(0), actual);
		assertArrayEquals(expectedQuestions.get(0).getAnswers().toArray(), actual.getAnswers().toArray());
	}

	@Test
	public void getQuestionByIdWithAnswersNullQuestion() {
		List<Question> questions = initQuestionList();
		questions.get(0).setAnswers(null);

		when(mockQuestionDao.findQuestionById(questions.get(0).getQuestionId())).thenReturn(null);

		Question actual = questionService.getQuestionByIdWithAnswers((questions.get(0).getQuestionId()));

		verify(mockQuestionDao, times(1)).findQuestionById(questions.get(0).getQuestionId());
		verify(mockAnswerService, times(0)).getAnswersByQuestion(anyObject());

		assertNull(actual);
	}

	private List<Answer> createAnswers() {
		List<Answer> res = new ArrayList<>();
		Answer answer1 = new Answer();
		answer1.setQuestionId(1);
		answer1.setAnswerText("answ1");
		answer1.setAnswerActive((byte) 1);
		answer1.setAnswerDefaultSortingOrder(0);
		answer1.setAnswerCorrect((byte) 1);

		Answer answer2 = new Answer();
		answer2.setQuestionId(1);
		answer2.setAnswerText("answ2");
		answer2.setAnswerActive((byte) 1);
		answer2.setAnswerDefaultSortingOrder(1);
		answer2.setAnswerCorrect((byte) 0);

		Answer answer3 = new Answer();
		answer3.setQuestionId(1);
		answer3.setAnswerText("answ3");
		answer3.setAnswerActive((byte) 1);
		answer3.setAnswerDefaultSortingOrder(2);
		answer3.setAnswerCorrect((byte) 0);

		res.add(answer1);
		res.add(answer2);
		res.add(answer3);

		return res;
	}

	private List<Question> initQuestionList() {
		List<Question> questions = new ArrayList<>();
		Question question1 = new Question();
		question1.setQuestionId(1);
		Answer answer1 = new Answer();
		answer1.setAnswerId(1);
		question1.addAnswer(answer1);
		Question question2 = new Question();
		question2.setQuestionId(2);
		question2.addAnswer(answer1);
		questions.add(question1);
		questions.add(question2);

		TestQuestionsBank testQuestionsBank1 = new TestQuestionsBank();
		testQuestionsBank1.setQuestionId(1);
		question1.addTestQuestionsBank(testQuestionsBank1);
		TestQuestionsBank testQuestionsBank2 = new TestQuestionsBank();
		testQuestionsBank2.setQuestionId(1);
		question1.addTestQuestionsBank(testQuestionsBank2);

		TestQuestionsBank testQuestionsBank3 = new TestQuestionsBank();
		testQuestionsBank3.setQuestionId(2);
		question2.addTestQuestionsBank(testQuestionsBank3);
		TestQuestionsBank testQuestionsBank4 = new TestQuestionsBank();
		testQuestionsBank4.setQuestionId(2);
		question2.addTestQuestionsBank(testQuestionsBank4);

		return questions;
	}

}
