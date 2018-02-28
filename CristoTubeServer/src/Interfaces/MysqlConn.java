package Interfaces;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author admin
 */
public interface MysqlConn {
    
     public static Connection getConn() throws SQLException{
        Connection conn=null;
       conn=DriverManager.getConnection("jdbc:mysql://52.19.19.65/cristotube", "testpsp" , "@,2,golfoPSP123abcd!");
        //conn=DriverManager.getConnection("jdbc:mysql://localhost/cristotube", "root" , "");
        return conn;
    }
}
