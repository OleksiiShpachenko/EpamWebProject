package com.shpach.tutor.view.customtags;

import java.io.IOException;
import java.util.OptionalDouble;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.service.TaskService;

/**
 * Custom tag which calculate average score by {@link Community}
 * 
 * @author Shpachenko_A_K
 *
 */
public class StudentCommunityAverageStatisticTag extends TagSupport {
	private static final Logger logger = Logger.getLogger(StudentCommunityAverageStatisticTag.class);

	private static final long serialVersionUID = 1L;

	private Community community;

	public void setCommunity(Community community) {
		this.community = community;
	}

	public int doStartTag() throws JspException {
		String res = "not Assigned";

		OptionalDouble count = community.getTests().stream()
				.filter(c -> (c.getTasks() != null && c.getTasks().size() > 0))
				.mapToInt(c -> TaskService.CalculateAverageScore(c.getTasks())).average();

		if (count.isPresent()) {
			res = Integer.toString((int) count.getAsDouble()) + "%";
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
