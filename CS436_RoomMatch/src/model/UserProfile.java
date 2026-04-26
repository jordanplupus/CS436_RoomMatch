package model;

import java.util.ArrayList;

public class UserProfile {
	private boolean loggedIn = false;
	private String username;
	
	private String sleepSchedule = "";
	private String cleanliness = "";
	private String guests = "";
	private ArrayList<String> preferences = new ArrayList<>();
	private boolean sleepDealbreaker = false;
	private boolean cleanlinessDealbreaker = false;
	private boolean guestsDealbreaker = false;
	private ArrayList<Boolean> dealbreakers = new ArrayList<>();
	
	public void setArrays(ArrayList<String> preferences, ArrayList<Boolean> dealbreakers) {
		clearPreferences();
		
		if( this.preferences.isEmpty() ) {
			for(String p : preferences )
				this.preferences.add(p);
		} else {
			for(int i=0; i<preferences.size(); i++) {
				if( i < this.preferences.size() )
					this.preferences.set(i,  preferences.get(i));
				else this.preferences.add(preferences.get(i));
			}
		}
		
		if (this.dealbreakers.isEmpty()) {
			for(boolean d : dealbreakers) {
				this.dealbreakers.add(d);
			}
		} else {
			for(int i=0; i<dealbreakers.size(); i++) {
				if( i < this.dealbreakers.size() ) 
					this.dealbreakers.set(i, dealbreakers.get(i));
				else this.dealbreakers.add(dealbreakers.get(i));
			}
		}
		
		checkCritError();
	}
	
	/*
	public void setPreferences(String sleep, String clean, String guest) {
	    sleepSchedule = sleep;
	    cleanliness = clean;
	    guests = guest;
	    
	    preferences.add(sleep);
	    preferences.add(clean);
	    preferences.add(guest);
	}*/
	
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
				if( i < this.preferences.size() ) 
					this.preferences.set(i, preferences.get(i));
				else this.preferences.add(preferences.get(i));
			}
		}
		
		//checkCritError();
	}
	
	public String getSleepSchedule() {
		return sleepSchedule != null ? sleepSchedule : "null";
	}
	
	public String getCleanliness() {
		return cleanliness != null ? cleanliness : "null";
	}
	
	/*
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
				if( i < this.dealbreakers.size() ) 
					this.dealbreakers.set(i, dealbreakers.get(i));
				else this.dealbreakers.add(dealbreakers.get(i));
			}
		}
		
	}*/

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
		
		checkCritError();
	}
	
	public void logout() {
		if( !loggedIn ) return;
		
		username = "";
		loggedIn = false;
		
		clearPreferences();
	}
	
	private void clearPreferences() {
		preferences.clear();
		dealbreakers.clear();
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
	
	public void checkCritError() {
		if( preferences.size() != dealbreakers.size() ) {
			System.out.println(preferences.toString() + "\n" + dealbreakers.toString());
			throw new IllegalArgumentException("A major error occurred, arrays do not align.\n"
					+ "Thrown in UserProfile.java");
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
