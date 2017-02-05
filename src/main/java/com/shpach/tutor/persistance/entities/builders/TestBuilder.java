package com.shpach.tutor.persistance.entities.builders;

import java.util.List;

import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.TestQuestionsBank;

public class TestBuilder {

	private Test test = new Test();

	public TestBuilder setTestFromTemplate(Test test) {
		if (test != null){
			this.test.setTestId(test.getTestId());
			this.test.setTestName(test.getTestName());
			this.test.setTestDescription(test.getTestDescription());
			this.test.setTestRndAnswer(test.getTestRndAnswer());
			this.test.setTestRndQuestion(test.getTestRndQuestion());
			this.test.setTestType(test.getTestType());
			this.test.setTestActive(test.getTestActive());
			this.test.setUserId(test.getUserId());
		}
		return this;
	}

	public TestBuilder setTestId(int testId) {
		test.setTestId(testId);
		return this;
	}

	public TestBuilder setTestName(String testName) {
		test.setTestName(testName);
		return this;
	}

	public TestBuilder setTestDescription(String testDescription) {
		test.setTestDescription(testDescription);
		return this;
	}

	public TestBuilder setTestActive(byte testActive) {
		test.setTestActive(testActive);
		return this;
	}

	public TestBuilder setTestRndQuestion(byte testRndQuestion) {
		test.setTestRndQuestion(testRndQuestion);
		return this;
	}

	public TestBuilder setTestRndAnswer(byte testRndAnswer) {
		test.setTestRndAnswer(testRndAnswer);
		return this;
	}

	public TestBuilder setTestType(int testType) {
		test.setTestType(testType);
		return this;
	}
	public TestBuilder setQuestions(List<TestQuestionsBank> questions) {
		test.setTestQuestionsBanks(questions);
		return this;
	}
	public Test buildForTestTake() {
		if(test.getTestQuestionsBanks()==null ||test.getTestQuestionsBanks().size()==0)
			return null;
		return test;
	}
}
