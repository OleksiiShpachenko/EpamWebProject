package com.shpach.tutor.persistance.jdbc.dao.factory;

import com.shpach.tutor.persistance.jdbc.dao.answer.IAnswerDao;
import com.shpach.tutor.persistance.jdbc.dao.answer.MySqlAnswerDao;
import com.shpach.tutor.persistance.jdbc.dao.answerlog.IAnswerLogDao;
import com.shpach.tutor.persistance.jdbc.dao.answerlog.MySqlAnswerLogDao;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.category.MySqlCategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.community.MySqlCommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;
import com.shpach.tutor.persistance.jdbc.dao.question.MySqlQuestionDao;
import com.shpach.tutor.persistance.jdbc.dao.questionlog.IQuestionLogDao;
import com.shpach.tutor.persistance.jdbc.dao.questionlog.MySqlQuestionLogDao;
import com.shpach.tutor.persistance.jdbc.dao.task.ITaskDao;
import com.shpach.tutor.persistance.jdbc.dao.task.MySqlTaskDao;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;
import com.shpach.tutor.persistance.jdbc.dao.test.MySqlTestDao;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.MySqlTestQuestionsBankDao;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.persistance.jdbc.dao.user.MySqlUserDao;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;
import com.shpach.tutor.persistance.jdbc.dao.userrole.MySqlUserRoleDao;

/**Implementation of {@link IDaoFactory} for MySql database 
 * @author Shpachenko_A_K
 *
 */
public class MySqlDaoFactory implements IDaoFactory {

	public IUserDao getUserDao() {
		return MySqlUserDao.getInstance();
	}

	public IUserRoleDao getUserRoleDao() {
		return MySqlUserRoleDao.getInstance();
	}

	public ICommunityDao getCommunityDao() {
		return MySqlCommunityDao.getInstance();
	}

	public ITestDao getTestDao() {
		return MySqlTestDao.getInstance();
	}

	@Override
	public ICategoryDao getCategoryDao() {
		return MySqlCategoryDao.getInstance();
	}

	@Override
	public IQuestionDao getQuestionDao() {
		return MySqlQuestionDao.getInstance();
	}

	@Override
	public ITestQuestionsBankDao getTestQuestionsBankDao() {
		return MySqlTestQuestionsBankDao.getInstance();
	}

	@Override
	public IAnswerDao getAnswerDao() {
		return MySqlAnswerDao.getInstance();
	}

	@Override
	public ITaskDao getTaskDao() {
		return MySqlTaskDao.getInstance();
	}

	@Override
	public IQuestionLogDao getQuestionLogDao() {
		return MySqlQuestionLogDao.getInstance();
	}

	@Override
	public IAnswerLogDao getAnswersLogDao() {
		return MySqlAnswerLogDao.getInstance();
	}

}
