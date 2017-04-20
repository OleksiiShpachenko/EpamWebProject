package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 * The persistent class for the answer database table.
 * 
 */
@Entity
@NamedQuery(name = "Answer.findAll", query = "SELECT a FROM Answer a")
public class Answer implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "answer_id")
	private int answerId;

	@Column(name = "answer_active")
	private byte answerActive;

	@Column(name = "answer_correct")
	private byte answerCorrect;

	@Column(name = "answer_default_sorting_order")
	private int answerDefaultSortingOrder;

	@Lob
	@Column(name = "answer_text")
	private String answerText;

	// bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	private int questionId;
	// bi-directional many-to-one association to AnswersLog
	@OneToMany(mappedBy = "answer")
	private List<AnswersLog> answersLogs;

	public Answer() {
	}

	public int getAnswerId() {
		return this.answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public byte getAnswerActive() {
		return this.answerActive;
	}

	public void setAnswerActive(byte answerActive) {
		this.answerActive = answerActive;
	}

	public byte getAnswerCorrect() {
		return this.answerCorrect;
	}

	public void setAnswerCorrect(byte answerCorrect) {
		this.answerCorrect = answerCorrect;
	}

	public int getAnswerDefaultSortingOrder() {
		return this.answerDefaultSortingOrder;
	}

	public void setAnswerDefaultSortingOrder(int answerDefaultSortingOrder) {
		this.answerDefaultSortingOrder = answerDefaultSortingOrder;
	}

	public String getAnswerText() {
		return this.answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
		setQuestionId(question.getQuestionId());
	}
	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public List<AnswersLog> getAnswersLogs() {
		return this.answersLogs;
	}

	public void setAnswersLogs(List<AnswersLog> answersLogs) {
		this.answersLogs = answersLogs;
	}

	public AnswersLog addAnswersLog(AnswersLog answersLog) {
		getAnswersLogs().add(answersLog);
		answersLog.setAnswer(this);

		return answersLog;
	}

	public AnswersLog removeAnswersLog(AnswersLog answersLog) {
		getAnswersLogs().remove(answersLog);
		answersLog.setAnswer(null);

		return answersLog;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + answerActive;
		result = prime * result + answerCorrect;
		result = prime * result + answerDefaultSortingOrder;
		result = prime * result + answerId;
		result = prime * result + ((answerText == null) ? 0 : answerText.hashCode());
		result = prime * result + questionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		if (answerActive != other.answerActive)
			return false;
		if (answerCorrect != other.answerCorrect)
			return false;
		if (answerDefaultSortingOrder != other.answerDefaultSortingOrder)
			return false;
		if (answerId != other.answerId)
			return false;
		if (answerText == null) {
			if (other.answerText != null)
				return false;
		} else if (!answerText.equals(other.answerText))
			return false;
		if (questionId != other.questionId)
			return false;
		return true;
	}

}