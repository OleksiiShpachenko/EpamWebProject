package com.shpach.tutor.view.sevrlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.shpach.tutor.commands.ICommand;
import com.shpach.tutor.manager.Config;
import com.shpach.tutor.manager.Message;

/**
 * @author Shpachenko_A_K
 *
 */
@WebServlet(name = "Controller", urlPatterns = { "/pages" })
public class Controller extends HttpServlet {
	private static final Logger logger = Logger.getLogger(Controller.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ControllerHelper controllerHelper = ControllerHelper.getInstance();

	public Controller() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = null;
		HttpServletRequest requestWrapped = null;
		try {
			ICommand command = controllerHelper.getCommand(request);
			page = command.execute(request, response);
			requestWrapped = controllerHelper.wrapRequest(request);
		} catch (ServletException e) {
			logger.error(e, e);
			request.setAttribute("messageError", Message.getInstance().getProperty(Message.SERVLET_EXECPTION));
			page = Config.getInstance().getProperty(Config.ERROR);
		} catch (IOException e) {
			logger.error(e, e);
			request.setAttribute("messageError", Message.getInstance().getProperty(Message.IO_EXCEPTION));
			page = Config.getInstance().getProperty(Config.ERROR);
		}
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
		dispatcher.forward(requestWrapped, response);
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on
	// the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
