package com.shpach.tutor.commands;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shpach.tutor.manager.Config;


public class CommandChangeLocale implements ICommand {

	
	public String execute(HttpServletRequest request, HttpServletResponse responce)
			throws ServletException, IOException {
		String page = Config.getInstance().getProperty(Config.LOGIN);
		boolean checkSession = false;
		String locale= request.getParameter("localeValue");
		Enumeration<String> attr= request.getSession().getAttributeNames();
		Map<String,String[]> lastRequest=(HashMap<String,String[]>)request.getSession().getAttribute("lastRequest");
		
		Map<String,String[]> lastRequest3=new HashMap<String, String[]>(request.getParameterMap());
		//request.getSession().setAttribute("lastRequest", request.getParameterMap());
		//HashMap<String,String[]> lastRequest2=(HashMap<String,String[]>)request.getSession().getAttribute("lastRequest");
		
		
		if(lastRequest!=null){
//			request=null;//new RequestWrapper(request,lastRequest);
			page= "/pages";
			//String comm= request.getParameter("command");		
			//String comm2=lastRequest.getParameter("command");
			int a=1;
		}
		request.getSession().setAttribute("javax.servlet.jsp.jstl.fmt.locale.session", locale);
		return page;
	}
}