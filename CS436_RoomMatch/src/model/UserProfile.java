package model;

import java.util.ArrayList;

public class UserProfile {
	private boolean loggedIn = false;
	private String username;
	
	private final int preferenceCount = 3;
	private String sleepSchedule = "";
	private String cleanliness = "";
	private String guests = "";
	private ArrayList<String> preferences = new ArrayList<>();
	private boolean sleepDealbreaker = false;
	private boolean cleanlinessDealbreaker = false;
	private boolean guestsDealbreaker = false;
	private ArrayList<Boolean> dealbreakers = new ArrayList<>();
	
	public void setPreferences(String sleep, String clean, String guest) {
	    sleepSchedule = sleep;
	    cleanliness = clean;
	    guests = guest;
	    
	    preferences.add(sleep);
	    preferences.add(clean);
	    preferences.add(guest);
	}
	
	public void setPreferences(java.util.List<String> preferences) {
		sleepSchedule = preferences.get(0);
		cleanliness = preferences.get(1);
		guests = preferences.get(2);
		
		if (this.preferences.isEmpty()) {
			for(int i=0; i<preferences.size(); i++) {
				this.preferences.add(preferences.get(i));
			}
		} else {
			for(int i=0; i<preferences.size(); i++) {
				this.preferences.set(i, preferences.get(i));
			}
		}
		
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
	    
	    dealbreakers.add(sleep);
	    dealbreakers.add(clean);
	    dealbreakers.add(guests);
	}
	
	public void setDealbreakers(java.util.List<Boolean> dealbreakers) {
		sleepDealbreaker = dealbreakers.get(0);
		cleanlinessDealbreaker = dealbreakers.get(1);
		guestsDealbreaker = dealbreakers.get(2);
		
		if (this.preferences.isEmpty()) {
			for(int i=0; i<dealbreakers.size(); i++) {
				this.dealbreakers.add(dealbreakers.get(i));
			}
		} else {
			for(int i=0; i<dealbreakers.size(); i++) {
				this.dealbreakers.set(i, dealbreakers.get(i));
			}
		}
		
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
	
	public ArrayList<String> getPreferencesAsArray() {
		return preferences;
	}
	
	public ArrayList<Boolean> getDealbreakersAsArray() {
		return dealbreakers;
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
