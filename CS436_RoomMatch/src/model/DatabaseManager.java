package model;

import java.sql.*;

public class DatabaseManager {
        final private String URL = "jdbc:sqlite:my.db";
        
        public void init() {
        	String sql = "CREATE TABLE IF NOT EXISTS accounts ("
        				+ "id INTEGER PRIMARY KEY,"
        				+ "name text NOT NULL,"
        				+ "password text NOT NULL"
        				+ ");";
        	try (Connection connection = DriverManager.getConnection(URL); 
        		 Statement stmnt = connection.createStatement()) {
                stmnt.execute(sql);
                System.out.println("Connection successful");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        
        // Needs Salting and Hashing for security
        public boolean insert(String user, String pass) {
        	if (isValid(user, pass)) {
        		System.out.println("Account already exits");
        		return false;
        	} else {
	        	String sql = "INSERT INTO accounts(name,password) VALUES(?,?)";
	        	try (Connection connection = DriverManager.getConnection(URL);  
	        		 PreparedStatement prepStmnt = connection.prepareStatement(sql)) {
	        		prepStmnt.setString(1, user);
	        		prepStmnt.setString(2, pass);
	        		prepStmnt.executeUpdate();
	        		System.out.println("User inserted");
	        		return true;
	        		
	        	} catch (SQLException e) {
	                System.err.println(e.getMessage());
	            }
        	}
        	return false;
        }
        
        public boolean isValid(String user, String pass) {
        	String sql = "SELECT * FROM accounts WHERE name = ?";
        	try (Connection connection = DriverManager.getConnection(URL);  
           		 PreparedStatement prepStmnt = connection.prepareStatement(sql)) {
           		prepStmnt.setString(1, user);
           		try (ResultSet rs = prepStmnt.executeQuery()) {
           			if (rs.next()) {
           				String pwd = rs.getString("password");
           				return pwd.equals(pass);
           			}
           			else {
           				return false;
           			}
           		}
           		
           	} catch (SQLException e) {
                   System.err.println(e.getMessage());
               }
        	return false;
        }
        
        
    }

