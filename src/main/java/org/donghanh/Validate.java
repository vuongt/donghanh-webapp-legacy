package org.donghanh;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Validate {
	public static boolean checkUser(String name, String pass, String type) 
    {
		boolean st =false;
		try{

			//loading drivers for mysql
			Class.forName(Parameters.JDBC_DRIVER);

			//creating connection with the database 
			Connection conn = DriverManager.getConnection
        		(Parameters.REGISTER_URL, Parameters.DB_USER, Parameters.DB_PASSWORD);
			PreparedStatement ps = conn.prepareStatement
        		("select * from register where name=? and pass=? and type=?");
			ps.setString(1, name);
			ps.setString(2, pass);
			ps.setString(3, type);
			ResultSet rs =ps.executeQuery();
			st = rs.next();
       
		} catch(Exception e) {
			e.printStackTrace();
		}
		return st;                 
    }	   
}
