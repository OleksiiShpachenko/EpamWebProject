/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.shpach.tutor.persistance.jdbc;

import java.sql.ResultSet;

import com.shpach.tutor.persistance.jdbc.connection.DBDriverBase;
import com.shpach.tutor.persistance.jdbc.connection.DBDriverFactory;
import com.shpach.tutor.persistance.jdbc.connection.DBTypes;
import com.shpach.tutor.persistance.jdbc.connection.Database;
import com.shpach.tutor.persistance.jdbc.core.Query;
/**
 *
 * @author epam
 */
public class TestConnection {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        DBDriverBase dbDriver = DBDriverFactory.createDriver(DBTypes.MYSQL);
       // dbDriver
        Database jdbcConn = new Database(dbDriver,"jdbc:mysql://localhost:3306/tutor_db", "sensej", "root");// "jdbc:mysql://localhost:3306/seexample", "root", "root");
        jdbcConn.connect();
        if (jdbcConn.validate()) {
        	/*
            Query query = new Query(jdbcConn);
            ResultSet rs = query.executeQuery("select * from employees");
            if (rs != null) {
                while (rs.next()) {
                    System.out.println("Name=" + rs.getString(2) + "; Position=" + rs.getString(3) + ";");
                }
            }
        } else {
            System.out.println("Database connection error. See log file for more info.");
            */
        }
        
        	System.out.println("OK");
    }

}
