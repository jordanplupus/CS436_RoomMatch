package model;

import java.sql.*;
import java.util.Comparator;
import java.util.Collections;

// TODO: The database should only be able to be accessed by an admin account. 
public class DatabaseManager {
        final private String URL = "jdbc:sqlite:my.db";
        
        public void init() {
        	String sql1 = "CREATE TABLE IF NOT EXISTS accounts ("
        				+ "id INTEGER PRIMARY KEY,"
        				+ "name text NOT NULL,"
        				+ "password text NOT NULL"
        				+ ");";
			String sql2 = "CREATE TABLE IF NOT EXISTS preferences ("
						+ "user_id INTEGER PRIMARY KEY,"
						+ "sleep_schedule TEXT,"
						+ "cleanliness TEXT,"
						+ "guests TEXT,"
						+ "FOREIGN KEY (user_id) REFERENCES accounts(id)"
						+ ");";
			String sql3 = "CREATE TABLE IF NOT EXISTS dealbreakers ("
			            + "user_id INTEGER PRIMARY KEY,"
			            + "sleep_schedule INTEGER DEFAULT 0,"
			            + "cleanliness INTEGER DEFAULT 0,"
			            + "guests INTEGER DEFAULT 0,"
			            + "FOREIGN KEY (user_id) REFERENCES accounts(id)"
			            + ");";
        	try (Connection connection = DriverManager.getConnection(URL); 
        		 Statement stmnt = connection.createStatement()) {
                stmnt.execute(sql1);
				stmnt.execute(sql2);
				stmnt.execute(sql3);
                System.out.println("Connection successful");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        
        // If this application is made public in the future then removing and re-adding 
        // the preferences table with new entries every time we make a new preference 
        // would be unrealistic. 
        // Every user would have to fill out their preferences all over again. 
        // So these two below methods are thus necessary.
        /**
         * Adds a column entry to the preferences table. 
         * @param columnName - name of the preference column to add
         */
        public void addPreferenceEntry(String columnName) {
        	String sql = "ALTER TABLE preferences "
        			   + "ADD " + columnName + " TEXT";
        	
        	try (Connection connection = DriverManager.getConnection(URL);
        		 PreparedStatement stmt = connection.prepareStatement(sql)) {
        		stmt.executeUpdate();
        		System.out.println("Added new table entry '" + columnName + "' to preferences");
		    } catch (SQLException e) {
		    	if( e.getMessage().substring(46, 67).equals("duplicate column name") ) 
		    		System.out.println("Table entry '" + columnName + "' already exists");
		    	else System.err.println(e.getMessage());
		    }
        }
        
        /**
         * Removes a column entry from the preferences table. 
         * @param columnName - name of the preference column to remove
         */
        public void removePreferenceEntry(String columnName) {
        	String sql = "ALTER TABLE preferences "
        			+ "DROP COLUMN " + columnName;
        	
        	try (Connection connection = DriverManager.getConnection(URL);
        		 PreparedStatement stmt = connection.prepareStatement(sql)) {
        		stmt.executeUpdate();
        	} catch (SQLException e) {
        		if( e.getMessage().substring(46, 60).equals("no such column") ) 
        			System.out.println("No column named " + columnName + " exists in the table");
        		else System.err.println(e.getMessage());
        	}
        }
        
        /**
         * Used to verify that the amount of preference entries matches entries in other
         * java classes. 
         * @return count of columns in preferences table
         */
        public int getPreferenceTableEntryCount() {
        	String sql = "SELECT COUNT(*) FROM pragma_table_info('preferences')";
        	
        	int count = -1;
        	
        	try (Connection connection = DriverManager.getConnection(URL);
           		 PreparedStatement stmt = connection.prepareStatement(sql)) {
           		ResultSet rs = stmt.executeQuery();
           		count = rs.getInt(1);
           	} catch (SQLException e) {
           		System.err.println(e.getMessage());
           	}
        	
        	return count;
        }
        
        //temp delete function
        public void delete(int userID) {
        	String sql1 = "DELETE FROM accounts WHERE id = ?";
        	String sql2 = "DELETE FROM preferences WHERE user_id = ?";
        	String sql3 = "DELETE FROM dealbreakers WHERE user_id = ?";

            try (Connection conn = DriverManager.getConnection(URL);
            		PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            		PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            		PreparedStatement pstmt3 = conn.prepareStatement(sql3);) {

                // Set the parameter for the WHERE clause
                pstmt1.setInt(1, userID);
                pstmt2.setInt(1, userID);
                pstmt3.setInt(1, userID);

                // Execute the DELETE statement
                pstmt1.executeUpdate();
                pstmt2.executeUpdate();
                pstmt3.executeUpdate();

                System.out.println("User " + userID + " Deleted");

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        

        // TODO Needs Salting and Hashing for security
        /**
         * Used to register a new user into the database. 
         * Returns true if user was successfully added. 
         * @param user - username
         * @param pass - password
         * @return true/false
         */
        public boolean insert(String user, String pass) {
            String checkSql = "SELECT * FROM accounts WHERE name = ?";
            try (Connection connection = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
                pstmt.setString(1, user);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Account already exists");
                    return false;
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                return false;
            }

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
            return false;
        }
        
        /**
         * Validates user credentials and returns true if username and password 
         * were inputed correctly. 
         * @param user - username
         * @param pass - password
         * @return true/false
         */
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
        
        /**
         * Returns id from account table of the specified user
         * @param username
         * @return database table id
         */
		public int getUserId(String username) {
			String sql = "SELECT id FROM accounts WHERE name = ?";
			try (Connection connection = DriverManager.getConnection(URL);
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					return rs.getInt("id");
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
			return -1;
		}

		/**
		 * Saves user preferences into the database. Adds a new preferences table 
		 * if it doesn't already exist. 
		 * @param userId - preferences table id to save to
		 * @param sleep
		 * @param cleanliness
		 * @param guests
		 */
		public void savePreferences(int userId, String sleep, String cleanliness, String guests) {
			String sql = "INSERT OR REPLACE INTO preferences (user_id, sleep_schedule, cleanliness, guests) "
					+ "VALUES (?, ?, ?, ?)";
			try (Connection connection = DriverManager.getConnection(URL);
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				pstmt.setString(2, sleep);
				pstmt.setString(3, cleanliness);
				pstmt.setString(4, guests);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}
		
		/**
		 * Returns a list of the users preferences. 
		 * @param userId - id of user in the preferences table
		 * @return ArrayList
		 */
		public java.util.List<String> getPreferences(int userId) {
			java.util.List<String> userPreferences = new java.util.ArrayList<>();
			String sql = "SELECT * FROM preferences WHERE user_id='" + userId + "'";
			try (Connection connection = DriverManager.getConnection(URL); 
				 PreparedStatement stmt = connection.prepareStatement(sql)) {
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					userPreferences.add(rs.getString("sleep_schedule"));
					userPreferences.add(rs.getString("cleanliness"));
					userPreferences.add(rs.getString("guests"));
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
			
			return userPreferences;
		}
		
		/**
		 * Saves deal-breaker settings for a user. Each preference can be marked
		 * as a deal-breaker (true) or flexible (false). Deal-breakers are stored
		 * as 1 or 0 in the database.
		 * @param userId - id of user in the accounts table
		 * @param sleep - true if sleep schedule is a deal-breaker
		 * @param cleanliness - true if cleanliness is a deal-breaker
		 * @param guests - true if guest frequency is a deal-breaker
		 */
		public void saveDealbreakers(int userId, boolean sleep, boolean cleanliness, boolean guests) {
		    String sql = "INSERT OR REPLACE INTO dealbreakers (user_id, sleep_schedule, cleanliness, guests) "
		            + "VALUES (?, ?, ?, ?)";
		    try (Connection connection = DriverManager.getConnection(URL);
		        PreparedStatement pstmt = connection.prepareStatement(sql)) {
		        pstmt.setInt(1, userId);
		        pstmt.setInt(2, sleep ? 1 : 0);
		        pstmt.setInt(3, cleanliness ? 1 : 0);
		        pstmt.setInt(4, guests ? 1 : 0);
		        pstmt.executeUpdate();
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    }
		}

		/**
		 * Returns deal-breaker settings for a user as a boolean array.
		 * Index 0 = sleep schedule, 1 = cleanliness, 2 = guests.
		 * Defaults to all false if no row exists for the user.
		 * @param userId - id of user in the accounts table
		 * @return boolean array of length 3
		 */
		public boolean[] getDealbreakers(int userId) {
		    boolean[] result = {false, false, false};
		    String sql = "SELECT sleep_schedule, cleanliness, guests FROM dealbreakers WHERE user_id = ?";
		    try (Connection connection = DriverManager.getConnection(URL);
		        PreparedStatement pstmt = connection.prepareStatement(sql)) {
		        pstmt.setInt(1, userId);
		        ResultSet rs = pstmt.executeQuery();
		        if (rs.next()) {
		            result[0] = rs.getInt("sleep_schedule") == 1;
		            result[1] = rs.getInt("cleanliness") == 1;
		            result[2] = rs.getInt("guests") == 1;
		        }
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    }
		    return result;
		}

		// For getting a list of everyone else's profiles except the user requesting a match, for comparing
		/**
		 * Gets and returns a list of all other user profiles that are not this user. 
		 * <p>Used to create compatibility scoring for this user to other users</p>
		 * @param excludeUserId - User id of this user
		 * @return ArrayList
		 */
		public java.util.List<UserProfile> getAllProfilesExcept(int excludeUserId) {
			java.util.List<UserProfile> profiles = new java.util.ArrayList<>();
			String sql = "SELECT a.id, a.name, p.sleep_schedule, p.cleanliness, p.guests "
					+ "FROM accounts a "
					+ "JOIN preferences p ON a.id = p.user_id "
					+ "WHERE a.id != ?";
			try (Connection connection = DriverManager.getConnection(URL);
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setInt(1, excludeUserId);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					UserProfile profile = new UserProfile();
					profile.login(rs.getString("name"));
					profile.setPreferences(
						rs.getString("sleep_schedule"),
						rs.getString("cleanliness"),
						rs.getString("guests")
					);
					profiles.add(profile);
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
			
			return profiles;
		}
		
		// Helper function for getting a profile based on user's ID
		/**
		 * Creates a UserProfile based on the found data when using user's ID
		 * @param userID - id of user in the accounts and preferences table
         * @return UserProfile
		 */
		private UserProfile getProfile(int userID) {
			String sql = "SELECT name FROM accounts WHERE id = ?";
			UserProfile result = new UserProfile();
			try (Connection connection = DriverManager.getConnection(URL);
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setInt(1, userID);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					result.login(rs.getString("name"));
					result.setPreferences(this.getPreferences(userID));
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
			return result;
		}
		
		// For getting a list of everyone else's profiles except the user requesting a match, and sorts the list in descending order
		/**
		 * Gets and returns a list of all other user profiles that are not this user and their compatability. 
		 * <p>Used to create compatibility scoring for this user to other users</p>
		 * @param excludeUserId - User id of this user
		 * @return ArrayList
		 */
		public java.util.List<SortProfiles> getAllMatches(int excludeUserId) {
			UserProfile currProfile = getProfile(excludeUserId);
			boolean[] dealbreakers = getDealbreakers(excludeUserId);
			java.util.List<SortProfiles> profiles = new java.util.ArrayList<>();
			//java.util.List<String> userPreferences = this.getPreferences(excludeUserId);
			String sql = "SELECT a.id, a.name, p.sleep_schedule, p.cleanliness, p.guests "
					+ "FROM accounts a "
					+ "JOIN preferences p ON a.id = p.user_id "
					+ "WHERE a.id != ?";
			try (Connection connection = DriverManager.getConnection(URL);
				PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setInt(1, excludeUserId);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					UserProfile profile = new UserProfile();
					profile.login(rs.getString("name"));
					profile.setPreferences(
						rs.getString("sleep_schedule"),
						rs.getString("cleanliness"),
						rs.getString("guests")
					);
					
					boolean filtered = false;
				    if (dealbreakers[0] && !currProfile.getSleepSchedule().equalsIgnoreCase(profile.getSleepSchedule())) {
				        filtered = true;
				    }
				    if (dealbreakers[1] && !currProfile.getCleanliness().equalsIgnoreCase(profile.getCleanliness())) {
				        filtered = true;
				    }
				    if (dealbreakers[2] && !currProfile.getGuests().equalsIgnoreCase(profile.getGuests())) {
				        filtered = true;
				    }
				    
				    if (!filtered) {
				        SortProfiles sProfile = new SortProfiles(currProfile, profile);
				        profiles.add(sProfile);
				    }
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
			Comparator<SortProfiles> myComparator = new SortByCompatabilty();
			Collections.sort(profiles, myComparator);
			return profiles;
		}
		
		
		// Just to see the data in our db
		public void printAllData() {
		    System.out.println("=== ACCOUNTS ===");
		    try (Connection conn = DriverManager.getConnection(URL);
		         Statement stmt = conn.createStatement();
		         ResultSet rs = stmt.executeQuery("SELECT * FROM accounts")) {
		        while (rs.next()) {
		            System.out.println("ID: " + rs.getInt("id") 
		                + " | Name: " + rs.getString("name") 
		                + " | Pass: " + rs.getString("password"));
		        }
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    }

		    System.out.println("=== PREFERENCES ===");
		    try (Connection conn = DriverManager.getConnection(URL);
		         Statement stmt = conn.createStatement();
		         ResultSet rs = stmt.executeQuery("SELECT * FROM preferences")) {
		        while (rs.next()) {
		            System.out.println("UserID: " + rs.getInt("user_id")
		                + " | Sleep: " + rs.getString("sleep_schedule")
		                + " | Clean: " + rs.getString("cleanliness")
		                + " | Guests: " + rs.getString("guests"));
		        }
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    }
		    
		    System.out.println("=== DEALBREAKERS ===");
		    try (Connection conn = DriverManager.getConnection(URL);
		         Statement stmt = conn.createStatement();
		         ResultSet rs = stmt.executeQuery("SELECT * FROM dealbreakers")) {
		        while (rs.next()) {
		            System.out.println("UserID: " + rs.getInt("user_id")
		                + " | Sleep: " + rs.getInt("sleep_schedule")
		                + " | Clean: " + rs.getInt("cleanliness")
		                + " | Guests: " + rs.getInt("guests"));
		        }
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    }
		}
        
		// For putting in an admin account, in case our db gets deleted or something
		public void seedAdmin() {
		    String checkSql = "SELECT * FROM accounts WHERE name = 'admin'";
		    try (Connection connection = DriverManager.getConnection(URL);
		         PreparedStatement pstmt = connection.prepareStatement(checkSql)) {
		        ResultSet rs = pstmt.executeQuery();
		        if (!rs.next()) {
		            insert("admin", "password");
		            int adminId = getUserId("admin");
		            savePreferences(adminId, "late", "medium", "sometimes");
		            saveDealbreakers(adminId, false, false, false);
		        }
		    } catch (SQLException e) {
		        System.err.println(e.getMessage());
		    }
		}
    }
