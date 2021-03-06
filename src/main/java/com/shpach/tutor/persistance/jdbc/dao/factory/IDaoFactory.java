package com.shpach.tutor.persistance.jdbc.dao.factory;

import com.shpach.tutor.persistance.jdbc.dao.answer.IAnswerDao;
import com.shpach.tutor.persistance.jdbc.dao.answerlog.IAnswerLogDao;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;
import com.shpach.tutor.persistance.jdbc.dao.questionlog.IQuestionLogDao;
import com.shpach.tutor.persistance.jdbc.dao.task.ITaskDao;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;

public interface IDaoFactory {
	IUserDao getUserDao();

	IUserRoleDao getUserRoleDao();

	ICommunityDao getCommunityDao();

	ITestDao getTestDao();

	ICategoryDao getCategoryDao();

	IQuestionDao getQuestionDao();

	ITestQuestionsBankDao getTestQuestionsBankDao();

	IAnswerDao getAnswerDao();

	ITaskDao getTaskDao();

	IQuestionLogDao getQuestionLogDao();

	IAnswerLogDao getAnswersLogDao();
}
