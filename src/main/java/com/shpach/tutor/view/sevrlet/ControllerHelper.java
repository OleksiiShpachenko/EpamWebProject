package com.shpach.tutor.view.sevrlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.shpach.tutor.commands.CommandAssignCategoryToTestDialog;
import com.shpach.tutor.commands.CommandAssignCommunityToTestDialog;
import com.shpach.tutor.commands.CommandAssignTestToCategory;
import com.shpach.tutor.commands.CommandAssignTestToCategoryDialog;
import com.shpach.tutor.commands.CommandAssignTestToCommunity;
import com.shpach.tutor.commands.CommandAssignTestToCommunityDialog;
import com.shpach.tutor.commands.CommandAssignUserToCommunity;
import com.shpach.tutor.commands.CommandChangeLocale;
import com.shpach.tutor.commands.CommandLogin;
import com.shpach.tutor.commands.CommandMissing;
import com.shpach.tutor.commands.CommandNewCategory;
import com.shpach.tutor.commands.CommandNewCommunity;
import com.shpach.tutor.commands.CommandTutorCategory;
import com.shpach.tutor.commands.CommandTutorCommunities;
import com.shpach.tutor.commands.CommandTutorQuestions;
import com.shpach.tutor.commands.CommandTutorTests;
import com.shpach.tutor.commands.ICommand;

/**
 *
 * @author KMM
 */
public class ControllerHelper {

	private static ControllerHelper instance = null;
	HashMap<String, ICommand> commands = new HashMap<String, ICommand>();

	private ControllerHelper() {
		commands.put("login", new CommandLogin());
		commands.put("tests", new CommandTutorTests());
		commands.put("locale", new CommandChangeLocale());
		commands.put("tutorCategories", new CommandTutorCategory());
		commands.put("newCategory", new CommandNewCategory());
		commands.put("tutorCommunities", new CommandTutorCommunities());
		commands.put("tutorQuestions", new CommandTutorQuestions());
		commands.put("newCommunity", new CommandNewCommunity());
		commands.put("assignUserToCommunity", new CommandAssignUserToCommunity());
		commands.put("assignTestToCommunityDialog", new CommandAssignTestToCommunityDialog());
		commands.put("assignTestToCommunity", new CommandAssignTestToCommunity());
		commands.put("assignTestToCategoryDialog", new CommandAssignTestToCategoryDialog());
		commands.put("assignTestToCategory", new CommandAssignTestToCategory());
		commands.put("assignCategoryToTestDialog", new CommandAssignCategoryToTestDialog());
		commands.put("assignCommunityToTestDialog", new CommandAssignCommunityToTestDialog());

	}

	public ICommand getCommand(HttpServletRequest request) {
		String param = request.getContextPath();// .getParameter("command");
		String param1 = request.getServletPath();
		String param2 = request.getPathInfo();
		String param3 = request.getPathTranslated();// .getPathInfo();
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

	public HttpServletRequest wrapRequest(HttpServletRequest request) {
		HttpServletRequest res = request;

		String commandText = request.getParameter("command");
		boolean setLastRequest = commandText.equals("locale") || commandText.equals("newCategory")
				|| commandText.equals("newCommunity") || commandText.equals("assignUserToCommunity")
				|| commandText.equals("assignTestToCommunityDialog") || commandText.equals("assignTestToCommunity")
				|| commandText.equals("assignTestToCategoryDialog") || commandText.equals("assignTestToCategory")
				|| commandText.equals("assignCategoryToTestDialog")
				|| commandText.equals("assignCommunityToTestDialog");

		if (setLastRequest) {
			Map<String, String[]> lastRequest = (HashMap<String, String[]>) request.getSession()
					.getAttribute("lastRequest");
			if (lastRequest != null)
				res = new RequestWrapper(request, lastRequest);

		} else if (commandText.equals("login")) {
			Map<String, String[]> lastRequest = new HashMap<>();
			lastRequest.put("command", new String[] { "tests" });

			if (lastRequest != null)
				res = new RequestWrapper(request, lastRequest);
		}

		/*
		 * switch (commandText) { case "locale": { Map<String, String[]>
		 * lastRequest = (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } case "login": {
		 * Map<String, String[]> lastRequest = new HashMap<>();
		 * lastRequest.put("command", new String[] { "tests" });
		 * 
		 * if (lastRequest != null) res = new RequestWrapper(request,
		 * lastRequest); break; } case "newCategory": { Map<String, String[]>
		 * lastRequest = (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } case "newCommunity": {
		 * Map<String, String[]> lastRequest = (HashMap<String, String[]>)
		 * request.getSession() .getAttribute("lastRequest"); if (lastRequest !=
		 * null) res = new RequestWrapper(request, lastRequest); break; } case
		 * "assignUserToCommunity": { Map<String, String[]> lastRequest =
		 * (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } case
		 * "assignTestToCommunityDialog": { Map<String, String[]> lastRequest =
		 * (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } case
		 * "assignTestToCommunity": { Map<String, String[]> lastRequest =
		 * (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } case
		 * "assignTestToCategoryDialog": { Map<String, String[]> lastRequest =
		 * (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } case
		 * "assignTestToCategory": { Map<String, String[]> lastRequest =
		 * (HashMap<String, String[]>) request.getSession()
		 * .getAttribute("lastRequest"); if (lastRequest != null) res = new
		 * RequestWrapper(request, lastRequest); break; } default:
		 * 
		 * }
		 */
		return res;
	}
}
