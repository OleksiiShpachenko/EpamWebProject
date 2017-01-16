package com.shpach.tutor.servise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.view.service.TutorMenu;

public class TutorServices {
	public static Map<String, Object> getParamForTutorMenu(User user){
		Map<String, Object> res=new HashMap<String, Object>();
		res.put("testsCount", TestService.getTestsCountByUser(user));
		return res;
	}
	public static List<TutorMenu> getTutorMenu(User user){
		List<TutorMenu> res = new ArrayList<TutorMenu>();
		TutorMenu tests= new TutorMenu("tutor.menu.test", TestService.getTestsCountByUser(user), "tests");
		TutorMenu questions= new TutorMenu("tutor.menu.questions", QuestionService.getQuestionsCountByUser(user), "tutorQuestions");
		TutorMenu communities= new TutorMenu("tutor.menu.communities", CommunityService.getCommunityCountByUser(user), "tutorCommunities");
		TutorMenu categories= new TutorMenu("tutor.menu.categories", CategoryService.getCategoriesCountByUser(user), "tutorCategories");
		
		res.add(tests);
		res.add(questions);
		res.add(communities);
		res.add(categories);
		return res;
	}
	public static void setActiveMenuByCommand( List<TutorMenu> tutorMenu, String command){
		for(TutorMenu item: tutorMenu){
			if (item.getCommand().equals(command))
				item.setActive(true);
		}
	}

}
