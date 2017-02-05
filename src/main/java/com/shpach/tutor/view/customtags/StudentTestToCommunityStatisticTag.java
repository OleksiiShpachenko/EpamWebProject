package com.shpach.tutor.view.customtags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Task;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.service.TaskService;

/**
 * Custom tag which calculate average score of passing current {@link Test} in
 * {@link Community}. If current {@link Test} not assigned to {@link Community}
 * return empty string. If current {@link Test} not passed (has not current
 * {@link Task}) return "Not passed" string
 * 
 * @author Shpachenko_A_K
 *
 */
public class StudentTestToCommunityStatisticTag extends TagSupport {
	private static final Logger logger = Logger.getLogger(StudentTestToCommunityStatisticTag.class);
	private static final long serialVersionUID = 1L;

	private Test test;
	private Community community;

	public void setTest(Test test) {
		this.test = test;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public int doStartTag() throws JspException {
		String res = "";

		long count = community.getTests().stream().filter(c -> c.getTestId() == test.getTestId()).count();

		if (count > 0) {
			int average = TaskService.CalculateAverageScore(test.getTasks());
			if (average == TaskService.EXLUDABLE_AVERAGE)
				res = "Not passed";
			else
				res = Integer.toString(average) + "%";
		}
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
