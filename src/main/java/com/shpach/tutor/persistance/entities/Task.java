package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the task database table.
 * 
 */
@Entity
@NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t")
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "task_id")
	private int taskId;

	@Column(name = "task_category_id")
	private int taskCategoryId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "task_datetime_start")
	private Date taskDatetimeStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "task_datetime_stop")
	private Date taskDatetimeStop;

	@Column(name = "task_score")
	private byte taskScore;

	// bi-directional many-to-one association to QuestionLog
	@OneToMany(mappedBy = "task")
	private List<QuestionLog> questionLogs;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private int userId;
	private User user;

	// bi-directional many-to-one association to Test
	@ManyToOne
	@JoinColumn(name = "test_id")
	private Test test;
	private int testId;

	public Task() {
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getTaskCategoryId() {
		return this.taskCategoryId;
	}

	public void setTaskCategoryId(int taskCategoryId) {
		this.taskCategoryId = taskCategoryId;
	}

	public Date getTaskDatetimeStart() {
		return this.taskDatetimeStart;
	}

	public void setTaskDatetimeStart(Date taskDatetimeStart) {
		this.taskDatetimeStart = taskDatetimeStart;
	}

	public Date getTaskDatetimeStop() {
		return this.taskDatetimeStop;
	}

	public void setTaskDatetimeStop(Date taskDatetimeStop) {
		this.taskDatetimeStop = taskDatetimeStop;
	}

	public byte getTaskScore() {
		return this.taskScore;
	}

	public void setTaskScore(byte taskScore) {
		this.taskScore = taskScore;
	}

	public List<QuestionLog> getQuestionLogs() {
		return this.questionLogs;
	}

	public void setQuestionLogs(List<QuestionLog> questionLogs) {
		this.questionLogs = questionLogs;
	}

	public QuestionLog addQuestionLog(QuestionLog questionLog) {
		if (getQuestionLogs() == null)
			questionLogs = new ArrayList<>();
		getQuestionLogs().add(questionLog);
		questionLog.setTask(this);

		return questionLog;
	}

	public QuestionLog removeQuestionLog(QuestionLog questionLog) {
		getQuestionLogs().remove(questionLog);
		questionLog.setTask(null);

		return questionLog;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		setUserId(user.getUserId());
	}

	public Test getTest() {
		return this.test;
	}

	public void setTest(Test test) {
		this.test = test;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

}