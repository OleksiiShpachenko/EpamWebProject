package com.shpach.tutor.view.customtags;

import java.io.IOException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.shpach.tutor.persistance.entities.User;
import com.shpach.tutor.persistance.entities.UsersRole;

/**Custom tag which show greeting line if user had authorized. 
 * @author Shpachenko_A_K
 *
 */
public class UserGreetingsTag extends TagSupport {
	private static final Logger logger = Logger.getLogger(UserGreetingsTag.class);

	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
		HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = req.getSession(false);

		User user = null;
		UsersRole usersRole = null;

		if (session != null) {
			if (session.getAttribute("userEntity") != null) {
				user = (User) session.getAttribute("userEntity");
				usersRole = (UsersRole) session.getAttribute("usersRole");
			}
		}

		String res = "";
		if (user != null) {
			String locale = (String) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
			ResourceBundle rs = null;
			if (locale != null)
				locale = "_" + locale;
			else
				locale = "_en_US";
			rs = ResourceBundle.getBundle("pagecontent" + locale);
			StringBuilder sb = new StringBuilder();

			sb.append("<ul class=\"upgrade-logout-nav\">");
			sb.append("<li><a href=\"#\">" + rs.getString("header.greetings") + "&#160; " + user.getUserName()
					+ "</a></li>");
			sb.append("<li><a href=\"#\">" + rs.getString("header.status") + ":&#160;" + usersRole.getRoleName()
					+ "</a></li>");
			sb.append("<li><form action=\"pages\" method=\"post\">");
			sb.append("<input type=\"hidden\" name=\"command\" value=\"logOut\" />");
			sb.append("<input class=\"simpleBtnLink logOut\" type=\"submit\" value=\"" + rs.getString("header.logOut")
					+ "\" />");
			sb.append("</form></li></ul></div>");
			res = sb.toString();
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
