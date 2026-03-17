package model;

public class UserProfile extends Observable {
	private boolean loggedIn = false;
	private String username;
	
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
}
