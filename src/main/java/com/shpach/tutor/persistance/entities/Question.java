package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the question database table.
 * 
 */
@Entity
@NamedQuery(name="Question.findAll", query="SELECT q FROM Question q")
public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="question_id")
	private int questionId;

	@Column(name="question_active")
	private byte questionActive;

	@Lob
	@Column(name="question_text")
	private String questionText;
	
	@Lob
	@Column(name="question_name")
	private String questionName;

	//bi-directional many-to-one association to Answer
	@OneToMany(mappedBy="question")
	private List<Answer> answers;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private int userId;

	//bi-directional many-to-one association to QuestionLog
	@OneToMany(mappedBy="question")
	private List<QuestionLog> questionLogs;

	//bi-directional many-to-one association to TestQuestionsBank
	@OneToMany(mappedBy="question")
	private List<TestQuestionsBank> testQuestionsBanks;

	public Question() {
	}

	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public byte getQuestionActive() {
		return this.questionActive;
	}

	public void setQuestionActive(byte questionActive) {
		this.questionActive = questionActive;
	}

	public String getQuestionText() {
		return this.questionText;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	public List<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	public Answer addAnswer(Answer answer) {
		getAnswers().add(answer);
		answer.setQuestion(this);

		return answer;
	}

	public Answer removeAnswer(Answer answer) {
		getAnswers().remove(answer);
		answer.setQuestion(null);

		return answer;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int id) {
		this.userId = id;
	}
	public List<QuestionLog> getQuestionLogs() {
		return this.questionLogs;
	}

	public void setQuestionLogs(List<QuestionLog> questionLogs) {
		this.questionLogs = questionLogs;
	}

	public QuestionLog addQuestionLog(QuestionLog questionLog) {
		getQuestionLogs().add(questionLog);
		questionLog.setQuestion(this);

		return questionLog;
	}

	public QuestionLog removeQuestionLog(QuestionLog questionLog) {
		getQuestionLogs().remove(questionLog);
		questionLog.setQuestion(null);

		return questionLog;
	}

	public List<TestQuestionsBank> getTestQuestionsBanks() {
		return this.testQuestionsBanks;
	}

	public void setTestQuestionsBanks(List<TestQuestionsBank> testQuestionsBanks) {
		this.testQuestionsBanks = testQuestionsBanks;
	}

	public TestQuestionsBank addTestQuestionsBank(TestQuestionsBank testQuestionsBank) {
		getTestQuestionsBanks().add(testQuestionsBank);
		testQuestionsBank.setQuestion(this);

		return testQuestionsBank;
	}

	public TestQuestionsBank removeTestQuestionsBank(TestQuestionsBank testQuestionsBank) {
		getTestQuestionsBanks().remove(testQuestionsBank);
		testQuestionsBank.setQuestion(null);

		return testQuestionsBank;
	}

}