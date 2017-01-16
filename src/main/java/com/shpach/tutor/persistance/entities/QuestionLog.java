package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the question_log database table.
 * 
 */
@Entity
@Table(name="question_log")
@NamedQuery(name="QuestionLog.findAll", query="SELECT q FROM QuestionLog q")
public class QuestionLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="question_log_id")
	private int questionLogId;

	@Column(name="question_default_sorting_order")
	private int questionDefaultSortingOrder;

	//bi-directional many-to-one association to AnswersLog
	@OneToMany(mappedBy="questionLog")
	private List<AnswersLog> answersLogs;

	//bi-directional many-to-one association to Task
	@ManyToOne
	@JoinColumn(name="task_id")
	private Task task;

	//bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name="question_id")
	private Question question;

	public QuestionLog() {
	}

	public int getQuestionLogId() {
		return this.questionLogId;
	}

	public void setQuestionLogId(int questionLogId) {
		this.questionLogId = questionLogId;
	}

	public int getQuestionDefaultSortingOrder() {
		return this.questionDefaultSortingOrder;
	}

	public void setQuestionDefaultSortingOrder(int questionDefaultSortingOrder) {
		this.questionDefaultSortingOrder = questionDefaultSortingOrder;
	}

	public List<AnswersLog> getAnswersLogs() {
		return this.answersLogs;
	}

	public void setAnswersLogs(List<AnswersLog> answersLogs) {
		this.answersLogs = answersLogs;
	}

	public AnswersLog addAnswersLog(AnswersLog answersLog) {
		getAnswersLogs().add(answersLog);
		answersLog.setQuestionLog(this);

		return answersLog;
	}

	public AnswersLog removeAnswersLog(AnswersLog answersLog) {
		getAnswersLogs().remove(answersLog);
		answersLog.setQuestionLog(null);

		return answersLog;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

}