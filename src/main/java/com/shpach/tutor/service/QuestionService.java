package com.shpach.tutor.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.shpach.tutor.persistance.entities.Answer;
import com.shpach.tutor.persistance.entities.Question;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.jdbc.dao.factory.IDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.factory.MySqlDaoFactory;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;

/**
 * Collection of services for {@link Question} entity class
 * 
 * @author Shpachenko_A_K
 *
 */
public class QuestionService {
	private static QuestionService instance;
	private IQuestionDao questionDao;
	private AnswerService answerService;
	private TestQuestionsBankService testQuestionsBankService;

	private QuestionService() {

	}

	public static synchronized QuestionService getInstance() {
		if (instance == null) {
			instance = new QuestionService();
		}
		return instance;
	}

	private IQuestionDao getQuestionDao() {
		if (questionDao == null) {
			IDaoFactory daoFactory = new MySqlDaoFactory();
			questionDao = daoFactory.getQuestionDao();
		}
		return questionDao;
	}

	private AnswerService getAnswerService() {
		if (answerService == null)
			answerService = AnswerService.getInstance();
		return answerService;
	}

	public TestQuestionsBankService getTestQuestionsBankService() {
		if (testQuestionsBankService == null)
			testQuestionsBankService = TestQuestionsBankService.getInstance();
		return testQuestionsBankService;
	}

	/**
	 * Gets count of {@link Question} assigned to {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return count of assigned {@link Question} to {@link User}
	 */
	public int getQuestionsCountByUser(User user) {
		if (user == null)
			return 0;
		List<Question> questions = getQuestionDao().findQuestionByUserId(user.getUserId());
		if (questions == null)
			return 0;
		return questions.size();
	}

	/**
	 * Gets list of {@link Question} from database by {@link User} with inserted
	 * assigned list of {@link Answer} and {@link Test}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return list of {@link Question}
	 */
	public List<Question> getQuestionsByUserWithAnswersAndTestsList(User user) {
		if (user == null)
			return new ArrayList<Question>();
		List<Question> questions = getQuestionDao().findQuestionByUserId(user.getUserId());
		if (questions == null)
			return new ArrayList<Question>();
		insertTestsToquestionsList(questions);
		insertAnswersToQuestionsList(questions);
		return questions;
	}

	/**
	 * Insert list of assigned {@link Answer} to list of {@link Question}
	 * 
	 * @param questions
	 *            - list of {@link Question}
	 */
	private void insertAnswersToQuestionsList(List<Question> questions) {
		for (Question item : questions) {
			List<Answer> answers = getAnswerService().getAnswersByQuestion(item);
			if (answers != null)
				item.setAnswers(answers);
		}
	}

	/**
	 * Insert list of assigned {@link Test} to list of {@link Question}
	 * 
	 * @param questions
	 *            - list of {@link Question}
	 */
	private void insertTestsToquestionsList(List<Question> questions) {
		for (Question item : questions) {
			List<TestQuestionsBank> testQuestionsBank = getTestQuestionsBankService()
					.getTestQuestionsBankWithTestInfoByQuestion(item);
			if (testQuestionsBank != null) {
				item.setTestQuestionsBanks(testQuestionsBank);
			}
		}
	}

	/**
	 * Add new {@link Question} to database
	 * 
	 * @param questionName
	 *            - short name of the question
	 * @param questionText
	 *            - question text
	 * @param questionAnswers
	 *            - array of Answers text of question
	 * @param questionAnswerCorrect
	 *            - array of indexes of questionAnswers array which is correct
	 * @param user
	 *            - {@link User} which is created the question
	 * @return
	 */
	public boolean addNewQuestion(String questionName, String questionText, String[] questionAnswers,
			String[] questionAnswerCorrect, User user) {
		if (user == null)
			return false;
		Question question = new Question();
		question.setQuestionName(questionName);
		question.setQuestionText(questionText);
		question.setUserId(user.getUserId());
		question.setQuestionActive((byte) 1);
		List<Answer> answers = createAnswersFromParams(questionAnswers, questionAnswerCorrect);
		question.setAnswers(answers);
		boolean questionValidation = validateQuestion(question);
		if (!questionValidation)
			return false;

		Question addedQuestion = getQuestionDao().addOrUpdate(question);
		if (addedQuestion == null)
			return false;
		for (Answer item : answers) {
			item.setQuestionId(addedQuestion.getQuestionId());
		}
		boolean isOk = getAnswerService().addNewAnswersList(answers);
		if (!isOk)
			return false;
		return true;
	}

	/**
	 * Create collection of {@link Answer} from arrays of Answers text and
	 * correct indexes
	 * 
	 * @param questionAnswers
	 *            - array of Answers text of question
	 * @param questionAnswerCorrect
	 *            - array of indexes of questionAnswers array which is correct
	 * @return collection of {@link Answer}
	 */
	private static List<Answer> createAnswersFromParams(String[] questionAnswers, String[] questionAnswerCorrect) {
		List<Answer> res = new ArrayList<>();
		List<String> correctAnswersIndexies = new ArrayList<>();
		if (questionAnswerCorrect != null)
			correctAnswersIndexies.addAll(Arrays.asList(questionAnswerCorrect));
		for (int i = 0; i < questionAnswers.length; i++) {
			if (!questionAnswers[i].equals("")) {
				Answer answer = new Answer();
				answer.setAnswerText(questionAnswers[i]);
				answer.setAnswerActive((byte) 1);
				answer.setAnswerDefaultSortingOrder(i);
				byte isCorrect = (correctAnswersIndexies.contains(Integer.toString(i))) ? (byte) 1 : 0;
				answer.setAnswerCorrect(isCorrect);
				res.add(answer);
			}
		}
		return res;
	}

	/**
	 * Validate {@link Question} with collection of {@link Answer} before adding
	 * to database. Question text and name should not be empty; should have at
	 * least two {@link Answer} and one of them should be correct
	 * 
	 * @param question
	 *            - {@link Question} entity class with with collection of
	 *            {@link Answer}
	 * @return true if validation Ok.
	 */
	private static boolean validateQuestion(Question question) {
		if (question.getQuestionName().equals(""))
			return false;
		else if (question.getQuestionText().equals(""))
			return false;
		else {
			List<Answer> res = question.getAnswers();
			if (res.size() < 2)
				return false;
			else if (!isAnswerListContainseCorrectOptions(res))
				return false;
		}
		return true;
	}

	/**
	 * Check if collection of {@link Answer} has at least one correct
	 * {@link Answer}
	 * 
	 * @param answers
	 *            - collection of {@link Answer}
	 * @return true if collection of {@link Answer} has at least one correct
	 *         {@link Answer}
	 */
	private static boolean isAnswerListContainseCorrectOptions(List<Answer> answers) {
		for (Answer item : answers) {
			if (item.getAnswerCorrect() == 1)
				return true;
		}
		return false;
	}

	/**
	 * Gets {@link Question} from database by id with collection of assigned
	 * collection of {@link Answer}
	 * 
	 * @param questionId
	 *            - id of {@link Question}
	 * @return {@link Question} with collection of assigned collection of
	 *         {@link Answer}
	 */
	public Question getQuestionByIdWithAnswers(int questionId) {
		Question question = getQuestionDao().findQuestionById(questionId);
		if (question != null) {
			List<Question> questions = new ArrayList<>();
			questions.add(question);
			insertAnswersToQuestionsList(questions);
		}
		return question;
	}
}
