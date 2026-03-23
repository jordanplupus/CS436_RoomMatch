package model;
import java.util.Comparator;

public class SortProfiles {
	private UserProfile currProfile;
	private String otherUser;
	private int score;
	
	public SortProfiles(UserProfile curr, UserProfile other) {
		currProfile = curr;
		score = MatchCalculator.calculateScore(curr, other);
		otherUser = other.getUser();
	}
	
	public UserProfile getUserProfile() {
		return currProfile;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getOtherUser() {
		return otherUser;
	}
}

class SortByCompatabilty implements Comparator {
	
	@Override
	public int compare(Object o1, Object o2) {
		SortProfiles curr = (SortProfiles) o1;
		SortProfiles other = (SortProfiles) o2;
		
		if (curr.getScore() > other.getScore()) {
			return -1;
		} else if (curr.getScore() < other.getScore()) {
			return 1;
		}
		return 0;
	}

}