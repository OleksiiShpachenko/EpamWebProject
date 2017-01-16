/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shpach.tutor.persistance.jdbc.connection;

import com.shpach.tutor.loggin.LoggerLoader;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;
/**
 *
 * @author epam
 */
public class DBDriverMysql extends DBDriverBase{
    private static final String DRIVER_CLASS = "org.gjt.mm.mysql.Driver";
    private static final Logger logger = LoggerLoader.getLogger(DBDriverMysql.class);

    public DBDriverMysql() throws ClassNotFoundException {
        super();
        registerDriver();
    }

    @Override
    protected String getDriverClass() {
        return DRIVER_CLASS;
    }

    @Override
    protected boolean registerDriver() {
        try{
        	
				Class.forName(DRIVER_CLASS);
	        // DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());
        } catch ( ClassNotFoundException  e) {//SQLException |
            logger.error(e, e);
            return false;
        }
        return true;
    }

    
}
