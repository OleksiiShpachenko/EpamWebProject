package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the answers_log database table.
 * 
 */
@Entity
@Table(name="answers_log")
@NamedQuery(name="AnswersLog.findAll", query="SELECT a FROM AnswersLog a")
public class AnswersLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="answer_log_id")
	private int answerLogId;

	@Column(name="answer_checked")
	private byte answerChecked;

	@Column(name="answer_log_sorting_order")
	private int answerLogSortingOrder;

	//bi-directional many-to-one association to Answer
	@ManyToOne
	@JoinColumn(name="answer_id")
	private Answer answer;
	private int answerId;

	//bi-directional many-to-one association to QuestionLog
	@ManyToOne
	@JoinColumn(name="question_log_id")
	private QuestionLog questionLog;

	public AnswersLog() {
	}

	public int getAnswerLogId() {
		return this.answerLogId;
	}

	public void setAnswerLogId(int answerLogId) {
		this.answerLogId = answerLogId;
	}

	public byte getAnswerChecked() {
		return this.answerChecked;
	}

	public void setAnswerChecked(byte answerChecked) {
		this.answerChecked = answerChecked;
	}

	public int getAnswerLogSortingOrder() {
		return this.answerLogSortingOrder;
	}

	public void setAnswerLogSortingOrder(int answerLogSortingOrder) {
		this.answerLogSortingOrder = answerLogSortingOrder;
	}

	public Answer getAnswer() {
		return this.answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public QuestionLog getQuestionLog() {
		return this.questionLog;
	}

	public void setQuestionLog(QuestionLog questionLog) {
		this.questionLog = questionLog;
	}

}