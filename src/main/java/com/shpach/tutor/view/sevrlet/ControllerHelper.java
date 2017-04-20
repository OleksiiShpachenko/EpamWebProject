package com.shpach.tutor.view.sevrlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shpach.tutor.commands.CommandAssignCategoryToTestDialog;
import com.shpach.tutor.commands.CommandAssignCommunityToTestDialog;
import com.shpach.tutor.commands.CommandAssignTestToCategory;
import com.shpach.tutor.commands.CommandAssignTestToCategoryDialog;
import com.shpach.tutor.commands.CommandAssignCategoryToCommunity;
import com.shpach.tutor.commands.CommandAssignTestToCommunityDialog;
import com.shpach.tutor.commands.CommandAssignTestToQuestion;
import com.shpach.tutor.commands.CommandAssignTestToQuestionDialog;
import com.shpach.tutor.commands.CommandAssignUserToCommunity;
import com.shpach.tutor.commands.CommandChangeLocale;
import com.shpach.tutor.commands.CommandLogOut;
import com.shpach.tutor.commands.CommandLogin;
import com.shpach.tutor.commands.CommandMissing;
import com.shpach.tutor.commands.CommandNewCategory;
import com.shpach.tutor.commands.CommandNewCommunity;
import com.shpach.tutor.commands.CommandNewQuestion;
import com.shpach.tutor.commands.CommandNewTest;
import com.shpach.tutor.commands.CommandNewTestDialog;
import com.shpach.tutor.commands.CommandRegistration;
import com.shpach.tutor.commands.CommandStudentStatistic;
import com.shpach.tutor.commands.CommandStudentTests;
import com.shpach.tutor.commands.CommandTakeTestAbort;
import com.shpach.tutor.commands.CommandTakeTestFinish;
import com.shpach.tutor.commands.CommandTakeTestSaveAnswer;
import com.shpach.tutor.commands.CommandTakeTestStart;
import com.shpach.tutor.commands.CommandTutorCategory;
import com.shpach.tutor.commands.CommandTutorCommunities;
import com.shpach.tutor.commands.CommandTutorQuestions;
import com.shpach.tutor.commands.CommandTutorStatistic;
import com.shpach.tutor.commands.CommandTutorTests;
import com.shpach.tutor.commands.ICommand;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.LoginService;

/**
 * @author Shpachenko_A_K
 *
 */
public class ControllerHelper {

	private static ControllerHelper instance = null;
	HashMap<String, ICommand> commands = new HashMap<String, ICommand>();

	private ControllerHelper() {
		commands.put("login", new CommandLogin());
		commands.put("tutorTests", new CommandTutorTests());
		commands.put("locale", new CommandChangeLocale());
		commands.put("tutorCategories", new CommandTutorCategory());
		commands.put("newCategory", new CommandNewCategory());
		commands.put("tutorCommunities", new CommandTutorCommunities());
		commands.put("tutorQuestions", new CommandTutorQuestions());
		commands.put("newCommunity", new CommandNewCommunity());
		commands.put("newQuestion", new CommandNewQuestion());
		commands.put("assignUserToCommunity", new CommandAssignUserToCommunity());
		commands.put("assignTestToCommunityDialog", new CommandAssignTestToCommunityDialog());
		commands.put("assignTestToCommunity", new CommandAssignCategoryToCommunity());
		commands.put("assignTestToCategoryDialog", new CommandAssignTestToCategoryDialog());
		commands.put("assignTestToCategory", new CommandAssignTestToCategory());
		commands.put("assignCategoryToTestDialog", new CommandAssignCategoryToTestDialog());
		commands.put("assignCommunityToTestDialog", new CommandAssignCommunityToTestDialog());
		commands.put("assignTestToQuestionDialog", new CommandAssignTestToQuestionDialog());
		commands.put("assignTestToQuestion", new CommandAssignTestToQuestion());
		commands.put("newTestDialog", new CommandNewTestDialog());
		commands.put("newTest", new CommandNewTest());
		commands.put("logOut", new CommandLogOut());
		commands.put("studentTests", new CommandStudentTests());
		commands.put("registration", new CommandRegistration());
		commands.put("takeTestStart", new CommandTakeTestStart());
		commands.put("takeTestSaveAnswer", new CommandTakeTestSaveAnswer());
		commands.put("takeTestFinish", new CommandTakeTestFinish());
		commands.put("takeTestAbort", new CommandTakeTestAbort());
		commands.put("studentStatistic", new CommandStudentStatistic());
		commands.put("tutorStatistic", new CommandTutorStatistic());

	}

	/**
	 * Return {@link ICommand} implementation according to "command" request
	 * parameter
	 * 
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return {@link ICommand} implementation
	 */
	public ICommand getCommand(HttpServletRequest request) {
		ICommand command = commands.get(request.getParameter("command"));
		if (command == null) {
			command = new CommandMissing();
		}
		return command;
	}

	public static ControllerHelper getInstance() {
		if (instance == null) {
			instance = new ControllerHelper();
		}
		return instance;
	}

	/**
	 * Wrap request according to "command" request parameter
	 * 
	 * @param request
	 *            - request {@link HttpServletRequest}
	 * @return wrapped {@link HttpServletRequest}
	 */
	public HttpServletRequest wrapRequest(HttpServletRequest request) {
		HttpServletRequest res = request;

		String commandText = request.getParameter("command");
		boolean setLastRequest = commandText.equals("locale") || commandText.equals("newCategory")
				|| commandText.equals("newCommunity") || commandText.equals("assignUserToCommunity")
				|| commandText.equals("assignTestToCommunityDialog") || commandText.equals("assignTestToCommunity")
				|| commandText.equals("assignTestToCategoryDialog") || commandText.equals("assignTestToCategory")
				|| commandText.equals("assignCategoryToTestDialog") || commandText.equals("assignCommunityToTestDialog")
				|| commandText.equals("newQuestion") || commandText.equals("assignTestToQuestionDialog")
				|| commandText.equals("assignTestToQuestion") || commandText.equals("newTestDialog")
				|| commandText.equals("newTest") || commandText.equals("takeTestFinish")
				|| commandText.equals("takeTestAbort");

		if (setLastRequest) {
			Map<String, String[]> lastRequest = (HashMap<String, String[]>) request.getSession()
					.getAttribute("lastRequest");
			if (lastRequest != null)
				res = new RequestWrapper(request, lastRequest);

		} else if (commandText.equals("login")) {
			Map<String, String[]> lastRequest = new HashMap<>();
			User user = (User) request.getSession().getAttribute("userEntity");
			String startCommand = "login";
			if (user != null) {
				startCommand = LoginService.getInstance().getStartCommandAccordingToUserRole(user);
			}
			lastRequest.put("command", new String[] { startCommand });
			if (lastRequest != null)
				res = new RequestWrapper(request, lastRequest);
		} else if (commandText.equals("takeTestSaveAnswer")) {
			Boolean lastQuestion = (Boolean) request.getSession().getAttribute("testLastQuestion");
			if (lastQuestion != null) {
				Map<String, String[]> lastRequest = new HashMap<>();
				lastRequest.put("command", new String[] { "takeTestFinish" });
				res = new RequestWrapper(request, lastRequest);
			}
		}
		return res;
	}
}
