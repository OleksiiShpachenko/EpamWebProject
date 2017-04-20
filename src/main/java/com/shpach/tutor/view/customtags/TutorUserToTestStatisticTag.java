package com.shpach.tutor.view.customtags;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.TaskService;

/**
 * Custom tag which calculate average score of passing current {@link Test} by
 * {@link User} in {@link Community}. If {@link Test} not assign to
 * {@link Community} return empty string. If {@link Test} did not passed by
 * {@link User} return "Not passed" string.
 * 
 * @author Shpachenko_A_K
 *
 */
public class TutorUserToTestStatisticTag extends TagSupport {
	private static final Logger logger = Logger.getLogger(TutorUserToTestStatisticTag.class);

	private static final long serialVersionUID = 1L;

	private int userId;
	private int testId;
	private Community community;

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public int doStartTag() throws JspException {

		String res = "";

		Test test =null; //TODO change insertParam to Category// community.getCategories().forEach(c->c.getTests().stream().filter(c -> c.getTestId() == testId).findFirst().get();
		List<Task> tasks = test.getTasks().stream().filter(c -> c.getUserId() == userId).collect(Collectors.toList());

		int average = TaskService.getInstance().CalculateAverageScore(tasks);
		if (average == TaskService.EXLUDABLE_AVERAGE)
			res = "Not passed";
		else
			res = Integer.toString(average) + "%";

		try {

			JspWriter out = pageContext.getOut();

			out.write(res);

		} catch (IOException e) {
			logger.error(e, e);
			throw new JspException(e.getMessage());

		}

		return SKIP_BODY;
	}
}
