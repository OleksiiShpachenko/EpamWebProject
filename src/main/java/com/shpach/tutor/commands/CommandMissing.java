package com.shpach.tutor.commands;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shpach.tutor.manager.Config;


/**Command which used for unknown commands
 * @author Shpachenko_A_K
 *
 */
public class CommandMissing implements ICommand {

   
    public String execute(HttpServletRequest request, HttpServletResponse responce) throws ServletException, IOException {
        return Config.getInstance().getProperty(Config.LOGIN);
    }
}
