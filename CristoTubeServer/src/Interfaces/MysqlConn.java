package Interfaces;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Alex Benavides
 */
public interface MysqlConn {
    
     public static Connection getConn() throws SQLException{
        Connection conn=null;
       conn=DriverManager.getConnection("address", "root" , "password");
        return conn;
    }
}
