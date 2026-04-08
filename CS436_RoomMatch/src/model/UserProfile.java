package model;

public class UserProfile {
	private boolean loggedIn = false;
	private String username;
	
	private final int preferenceCount = 3;
	private String sleepSchedule = "";
	private String cleanliness = "";
	private String guests = "";
	private boolean sleepDealbreaker = false;
	private boolean cleanlinessDealbreaker = false;
	private boolean guestsDealbreaker = false;
	
	public void setPreferences(String sleep, String clean, String guest) {
	    sleepSchedule = sleep;
	    cleanliness = clean;
	    guests = guest;
	}
	
	public void setPreferences(java.util.List<String> preferences) {
		sleepSchedule = preferences.get(0);
		cleanliness = preferences.get(1);
		guests = preferences.get(2);
	}
	
	public int getPreferenceCount() {
		return preferenceCount;
	}
	
	public String getSleepSchedule() {
		return sleepSchedule != null ? sleepSchedule : "null";
	}
	
	public String getCleanliness() {
		return cleanliness != null ? cleanliness : "null";
	}
	
	public void setDealbreakers(boolean sleep, boolean clean, boolean guests) {
	    sleepDealbreaker = sleep;
	    cleanlinessDealbreaker = clean;
	    guestsDealbreaker = guests;
	}

	public boolean isSleepDealbreaker() {
	    return sleepDealbreaker;
	}

	public boolean isCleanlinessDealbreaker() {
	    return cleanlinessDealbreaker;
	}

	public boolean isGuestsDealbreaker() {
	    return guestsDealbreaker;
	}
	
	public String getGuests() {
		return guests != null ? guests : "null";
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
	
	public String[] getPreferencesAsArray() {
		String arr[] = new String[3];
		
		arr[0] = sleepSchedule;
		arr[1] = cleanliness;
		arr[2] = guests;
		
		return arr;
	}
	
	public void verifyPreferenceCount(int shouldBe) {
		if( preferenceCount > shouldBe ) {
			throw new IllegalArgumentException("There are more preferences in 'UserProfile.java' than exist in the database");
		}
		if( preferenceCount < shouldBe ) {
			throw new IllegalArgumentException("There are more preferences in the database than exist in 'UserProfile'");
		}
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
