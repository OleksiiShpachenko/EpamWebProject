package com.shpach.tutor.persistance.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the test_questions_bank database table.
 * 
 */
@Entity
@Table(name = "test_questions_bank")
@NamedQuery(name = "TestQuestionsBank.findAll", query = "SELECT t FROM TestQuestionsBank t")
public class TestQuestionsBank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "test_questions_bank_id")
	private int testQuestionsBankId;

	@Column(name = "question_default_sorting_order")
	private int questionDefaultSortingOrder;

	// bi-directional many-to-one association to Test
	@ManyToOne
	@JoinColumn(name = "test_id")
	private Test test;
	private int testId;

	// bi-directional many-to-one association to Question
	@ManyToOne
	@JoinColumn(name = "question_id")
	private Question question;
	private int questionId;

	public TestQuestionsBank() {
	}

	public int getTestQuestionsBankId() {
		return this.testQuestionsBankId;
	}

	public void setTestQuestionsBankId(int testQuestionsBankId) {
		this.testQuestionsBankId = testQuestionsBankId;
	}

	public int getQuestionDefaultSortingOrder() {
		return this.questionDefaultSortingOrder;
	}

	public void setQuestionDefaultSortingOrder(int questionDefaultSortingOrder) {
		this.questionDefaultSortingOrder = questionDefaultSortingOrder;
	}

	public Test getTest() {
		return this.test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
	public int getTestId() {
		return this.testId;
	}

	public void setTestId(int id) {
		this.testId = id;
	}
	public Question getQuestion() {
		return this.question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	public int getQuestionId() {
		return this.questionId;
	}

	public void setQuestionId(int id) {
		this.questionId = id;
	}

}