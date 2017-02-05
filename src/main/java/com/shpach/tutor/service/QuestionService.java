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
	/**
	 * Gets count of {@link Question} assigned to {@link User}
	 * 
	 * @param user
	 *            - {@link User} entity class
	 * @return count of assigned {@link Question} to {@link User}
	 */
	public static int getQuestionsCountByUser(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		List<Question> questions = questionDao.findQuestionByUserId(user.getUserId());
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
	public static List<Question> getQuestionsByUserWithAnswersAndTestsList(User user) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		List<Question> questions = questionDao.findQuestionByUserId(user.getUserId());
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
	private static void insertAnswersToQuestionsList(List<Question> questions) {
		for (Question item : questions) {
			List<Answer> answers = AnswerService.getAnswersByQuestion(item);
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
	private static void insertTestsToquestionsList(List<Question> questions) {
		for (Question item : questions) {
			List<TestQuestionsBank> testQuestionsBank = TestQuestionsBankService
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
	public static boolean addNewQuestion(String questionName, String questionText, String[] questionAnswers,
			String[] questionAnswerCorrect, User user) {
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
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		Question addedQuestion = questionDao.addOrUpdate(question);
		if (addedQuestion == null)
			return false;
		for (Answer item : answers) {
			item.setQuestionId(addedQuestion.getQuestionId());
		}
		boolean isOk = AnswerService.addNewAnswersList(answers);
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
		List<String> correctAnswersIndexies = new ArrayList<>(Arrays.asList(questionAnswerCorrect));
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
	public static Question getQuestionByIdWithAnswers(int questionId) {
		IDaoFactory daoFactory = new MySqlDaoFactory();
		IQuestionDao questionDao = daoFactory.getQuestionDao();
		Question question = questionDao.findQuestionById(questionId);
		List<Question> questions = new ArrayList<>();
		questions.add(question);
		insertAnswersToQuestionsList(questions);
		return question;
	}

}
