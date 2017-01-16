package com.shpach.tutor.servise;

public class SessionServise {
	public static boolean checkSession(String sessionId){
		return false;
	}
	public static boolean checkSession(String sessionId, String userParam){
		if (userParam!=null)
			return true;
		return false;
	}
}
