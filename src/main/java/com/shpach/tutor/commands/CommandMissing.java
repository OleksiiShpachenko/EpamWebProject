/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shpach.tutor.commands;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shpach.tutor.manager.Config;

/**
 *
 * @author MAXIM
 */
public class CommandMissing implements ICommand {

   
    public String execute(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException {
        return Config.getInstance().getProperty(Config.LOGIN);
    }
}
