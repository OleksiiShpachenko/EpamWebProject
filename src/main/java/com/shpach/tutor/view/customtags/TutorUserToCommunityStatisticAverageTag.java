package com.shpach.tutor.view.customtags;

import java.io.IOException;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.Community;
import com.shpach.tutor.persistance.entities.Test;
import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.service.TaskService;

/**
 * Custom tag which calculate average score of passing all {@link Test} in
 * {@link Community} by {@link User}
 * 
 * @author Shpachenko_A_K
 *
 */
public class TutorUserToCommunityStatisticAverageTag extends TagSupport {
	private static final Logger logger = Logger.getLogger(TutorUserToCommunityStatisticAverageTag.class);

	private static final long serialVersionUID = 1L;

	private int userId;
	private Community community;

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public int doStartTag() throws JspException {
		String res = "";

		OptionalDouble count = null;
		if (community.getTests() != null) {
			count = community.getTests().stream()
					.mapToInt(c -> TaskService.CalculateAverageScore(
							c.getTasks().stream().filter(a -> a.getUserId() == userId).collect(Collectors.toList())))
					.filter(c -> c != TaskService.EXLUDABLE_AVERAGE).average();
		}
		if (count != null && count.isPresent()) {
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
