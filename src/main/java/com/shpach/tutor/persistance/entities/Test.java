package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the test database table.
 * 
 */
@Entity
@NamedQuery(name = "Test.findAll", query = "SELECT t FROM Test t")
public class Test implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "test_id")
	private int testId;

	@Column(name = "test_active")
	private byte testActive;

	@Column(name = "test_description")
	private String testDescription;

	@Column(name = "test_name")
	private String testName;

	@Column(name = "test_rnd_answer")
	private byte testRndAnswer;

	@Column(name = "test_rnd_question")
	private byte testRndQuestion;

	@Column(name = "test_type")
	private int testType;

	// bi-directional many-to-one association to Task
	@OneToMany(mappedBy = "test")
	private List<Task> tasks;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	private int userId;
	// bi-directional many-to-one association to TestQuestionsBank
	@OneToMany(mappedBy = "test")
	private List<TestQuestionsBank> testQuestionsBanks;

	// bi-directional many-to-many association to Category
	@ManyToMany
	@JoinTable(name = "test_to_category_relationship", joinColumns = {
			@JoinColumn(name = "test_id") }, inverseJoinColumns = { @JoinColumn(name = "category_id") })
	private List<Category> categories;

	

	public Test() {
	}

	public int getTestId() {
		return this.testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public byte getTestActive() {
		return this.testActive;
	}

	public void setTestActive(byte testActive) {
		this.testActive = testActive;
	}

	public String getTestDescription() {
		return this.testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public String getTestName() {
		return this.testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public byte getTestRndAnswer() {
		return this.testRndAnswer;
	}

	public void setTestRndAnswer(byte testRndAnswer) {
		this.testRndAnswer = testRndAnswer;
	}

	public byte getTestRndQuestion() {
		return this.testRndQuestion;
	}

	public void setTestRndQuestion(byte testRndQuestion) {
		this.testRndQuestion = testRndQuestion;
	}

	public int getTestType() {
		return this.testType;
	}

	public void setTestType(int testType) {
		this.testType = testType;
	}

	public List<Task> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public Task addTask(Task task) {
		getTasks().add(task);
		task.setTest(this);

		return task;
	}

	public Task removeTask(Task task) {
		getTasks().remove(task);
		task.setTest(null);

		return task;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		setUserId(user.getUserId());
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public List<TestQuestionsBank> getTestQuestionsBanks() {
		return this.testQuestionsBanks;
	}

	public void setTestQuestionsBanks(List<TestQuestionsBank> testQuestionsBanks) {
		this.testQuestionsBanks = testQuestionsBanks;
	}

	public TestQuestionsBank addTestQuestionsBank(TestQuestionsBank testQuestionsBank) {
		getTestQuestionsBanks().add(testQuestionsBank);
		testQuestionsBank.setTest(this);

		return testQuestionsBank;
	}

	public TestQuestionsBank removeTestQuestionsBank(TestQuestionsBank testQuestionsBank) {
		getTestQuestionsBanks().remove(testQuestionsBank);
		testQuestionsBank.setTest(null);

		return testQuestionsBank;
	}

	public List<Category> getCategories() {
		return this.categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + testActive;
		result = prime * result + ((testDescription == null) ? 0 : testDescription.hashCode());
		result = prime * result + testId;
		result = prime * result + ((testName == null) ? 0 : testName.hashCode());
		result = prime * result + testRndAnswer;
		result = prime * result + testRndQuestion;
		result = prime * result + testType;
		result = prime * result + userId;
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
		Test other = (Test) obj;
		if (testActive != other.testActive)
			return false;
		if (testDescription == null) {
			if (other.testDescription != null)
				return false;
		} else if (!testDescription.equals(other.testDescription))
			return false;
		if (testId != other.testId)
			return false;
		if (testName == null) {
			if (other.testName != null)
				return false;
		} else if (!testName.equals(other.testName))
			return false;
		if (testRndAnswer != other.testRndAnswer)
			return false;
		if (testRndQuestion != other.testRndQuestion)
			return false;
		if (testType != other.testType)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Test [testId=" + testId + ", testActive=" + testActive + ", testDescription=" + testDescription
				+ ", testName=" + testName + ", testRndAnswer=" + testRndAnswer + ", testRndQuestion=" + testRndQuestion
				+ ", testType=" + testType + ", userId=" + userId + "]";
	}

}