package model;
import java.util.Comparator;

public class SortProfiles {
	private UserProfile currProfile;
	private UserProfile otherProfile;
	private int score;
	
	public SortProfiles(UserProfile curr, UserProfile other) {
		currProfile = curr;
		otherProfile = other;
		score = MatchCalculator.calculateScore(curr, other);
	}
	
	public UserProfile getUserProfile() {
		return currProfile;
	}
	
	public UserProfile getOtherProfile() {
		return otherProfile;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getOtherUser() {
		return otherProfile.getUser();
	}
}

class SortByCompatabilty implements Comparator<SortProfiles> {
	@Override
	public int compare(SortProfiles curr, SortProfiles other) {
		if (curr.getScore() > other.getScore()) {
			return -1;
		} else if (curr.getScore() < other.getScore()) {
			return 1;
		}
		return 0;
	}
}