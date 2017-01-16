/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shpach.tutor.persistance.jdbc.dao.factory;

import java.sql.SQLException;

import com.shpach.tutor.persistance.jdbc.connection.DBDriverBase;
import com.shpach.tutor.persistance.jdbc.connection.DBDriverFactory;
import com.shpach.tutor.persistance.jdbc.connection.DBTypes;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.dao.category.ICategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.category.MySqlCategoryDao;
import com.shpach.tutor.persistance.jdbc.dao.community.ICommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.community.MySqlCommunityDao;
import com.shpach.tutor.persistance.jdbc.dao.question.IQuestionDao;
import com.shpach.tutor.persistance.jdbc.dao.question.MySqlQuestionDao;
import com.shpach.tutor.persistance.jdbc.dao.test.ITestDao;
import com.shpach.tutor.persistance.jdbc.dao.test.MySqlTestDao;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.ITestQuestionsBankDao;
import com.shpach.tutor.persistance.jdbc.dao.testquestionbank.MySqlTestQuestionsBankDao;
import com.shpach.tutor.persistance.jdbc.dao.user.IUserDao;
import com.shpach.tutor.persistance.jdbc.dao.user.MySqlUserDao;
import com.shpach.tutor.persistance.jdbc.dao.userrole.IUserRoleDao;
import com.shpach.tutor.persistance.jdbc.dao.userrole.MySqlUserRoleDao;

/**
 *
 * @author epam
 */
public class MySqlDaoFactory implements IDaoFactory {

	/*
	 * public static EmployeesDao create(Database db) { return new
	 * EmployeesDao(db); }
	 */
	public static Database createConnection() {
		DBDriverBase dbDriver = null;
		Database jdbcConn = null;
		try {
			dbDriver = DBDriverFactory.createDriver(DBTypes.MYSQL);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// dbDriver
		try {
			jdbcConn = new Database(dbDriver, "jdbc:mysql://localhost:3306/tutor", "sensej", "root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // "jdbc:mysql://localhost:3306/seexample", "root", "root");
		try {
			jdbcConn.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jdbcConn;
	}

	public IUserDao getUserDao() {
		return new MySqlUserDao();
	}

	public IUserRoleDao getUserRoleDao() {
		return new MySqlUserRoleDao();
	}

	public ICommunityDao getCommunityDao() {
		return new MySqlCommunityDao();
	}

	public ITestDao getTestDao() {
		return new MySqlTestDao();
	}

	@Override
	public ICategoryDao getCategoryDao() {
		return new MySqlCategoryDao();
	}

	@Override
	public IQuestionDao getQuestionDao() {
		return new MySqlQuestionDao();
	}

	@Override
	public ITestQuestionsBankDao getTestQuestionsBankDao() {
		return new MySqlTestQuestionsBankDao();
	}
	
}
