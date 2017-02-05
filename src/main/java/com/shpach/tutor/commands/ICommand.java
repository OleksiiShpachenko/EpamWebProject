package com.shpach.tutor.commands;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Shpachenko_A_K
 *
 */
public interface ICommand {

    public String execute(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException;
}
