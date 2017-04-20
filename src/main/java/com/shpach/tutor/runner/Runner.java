package com.shpach.tutor.runner;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.UserService;

public class Runner {

	public static void main(String[] args) {
		User bestWorstUser=UserService.getInstance().findUserWithGreatWorstStatistic();
		System.out.println(bestWorstUser);

	}

}
