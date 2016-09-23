package edu.weber.resptherapy.charting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

/**
 * Class for handling connection to the database
 */
public class DatabaseConnector {
	
	private Connection _dbConnection;
	
	//______________________________________________________________________________________
		
	//open the database and connect
	public Connection connectDatabase() throws SQLException{
		
		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		//ip address
//		String host = "127.2.85.130"; //PRODUCTION
		String host = "127.0.0.1"; //TEST
		//port
		String port = "3306";
		
		String appName = "weberstate";
		
		//the database JDBC URL
		String url = "jdbc:mysql://" + host + ":" + port + "/" + appName;
		
		//the database username
		String username = "adminFxASVVU"; //adminFxASVVU
		
		//the database password
		String password = "Q2lcMxlMgS_e"; //Q2lcMxlMgS_e
		
//		String completeUrl = "";
		
		//get connection by passing in the URL, username, and password for the database
		try {
			_dbConnection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		//return the connection to the database
		return _dbConnection;
	}
	
	//______________________________________________________________________________________
	
	//get the current database connection, which may or may not have an active connection
	public Connection getConnection(){
		
		return _dbConnection;
	}
	
	//______________________________________________________________________________________
	
	public void closeConnection(){
		
		try {
			
			_dbConnection.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	//______________________________________________________________________________________
	
}
