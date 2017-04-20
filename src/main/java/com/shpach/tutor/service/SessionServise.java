package com.shpach.tutor.service;

/**
 * Collection of services for managing users session
 * 
 * @author Shpachenko_A_K
 *
 */
public class SessionServise {
	private static SessionServise instance = null;

	private SessionServise() {

	}

	public static synchronized SessionServise getInstance() {
		if (instance == null) {
			instance = new SessionServise();
		}
		return instance;
	}

	/**
	 * Validate users session
	 * 
	 * @param sessionId
	 *            - session id
	 * @param userParam
	 *            - users attribute which should not be empty
	 * @return true if validation is Ok
	 */
	public boolean checkSession(String sessionId, String userParam) {
		if (userParam != null)
			return true;
		return false;
	}
}
