package model;

public class UserProfile extends Observable {
	private boolean loggedIn = false;
	private String username;
	// prevent null issues
	private String sleepSchedule = "";
	private String cleanliness = "";
	private String guests = "";
	
	public void setPreferences(String sleep, String clean, String guest) {
	    sleepSchedule = sleep;
	    cleanliness = clean;
	    guests = guest;
	}
	
	public String getSleepSchedule() {
		return sleepSchedule;
	}
	
	public String getCleanliness() {
		return cleanliness;
	}
	
	public String getGuests() {
		return guests;
	}

	public String getUsername() {
	    return username;
	}
	
	public void login(String user) {
		if( loggedIn ) {
			System.out.println("User already logged in");
			return;
		}
		
		username = user;
		loggedIn = true;
	}
	
	public void logout() {
		if( !loggedIn ) return;
		
		username = "";
		loggedIn = false;
	}
	
	public String getUser() {
		if( loggedIn )
			return username;
		
		return null;
	}

	// for debugging
	@Override
	public String toString() {
	    return "UserProfile{" +
	            "username='" + username + '\'' +
	            ", sleep='" + sleepSchedule + '\'' +
	            ", cleanliness='" + cleanliness + '\'' +
	            ", guests='" + guests + '\'' +
	            '}';
	}
}
