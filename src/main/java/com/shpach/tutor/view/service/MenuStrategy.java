package com.shpach.tutor.view.service;

import java.util.List;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.UserMenuService;


public class MenuStrategy {
	private List<UserMenuItem> userMenu;
	public final static int USER_ROLE_TUTOR = 1;
	public final static int USER_ROLE_STUDENT = 2;
	public MenuStrategy(User user) {
		if (user==null)
			return;
		switch(user.getRoleId()){
		case USER_ROLE_TUTOR: {
			userMenu=UserMenuService.getTutorMenu(user);
			break;
		}
		case USER_ROLE_STUDENT: {
			userMenu=UserMenuService.getStudentMenu(user);
			break;
		}
		default: {
			userMenu=UserMenuService.getStudentMenu(user);
			break;
		}
		}
	}
	public List<UserMenuItem> getMenu(){
		return userMenu;
	}

}
