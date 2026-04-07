package model;

public abstract class Preferences {
	// prevent null issues
	private String sleepSchedule = "";
	private String cleanliness = "";
	private String guests = "";
	
	public abstract String getSleepSchedule();
	
	public abstract String getCleanliness();
	
	public abstract String getGuests();
	
	public String[] getPreferencesAsArray() {
		String arr[] = new String[3];
		
		arr[0] = sleepSchedule;
		arr[1] = cleanliness;
		arr[2] = guests;
		
		return arr;
	}
}
