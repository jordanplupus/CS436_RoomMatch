package model;

import java.util.ArrayList;

public class MatchCalculator {

    public static int calculateScore(UserProfile a, UserProfile b) {
        int score = 0;
        ArrayList<String> weights = ReadWrite.RetrieveFileAsTextArr("/txt/weights.txt");
        String s[];

        ArrayList<String> aPrefs = a.getPreferencesAsArray();
        ArrayList<String> bPrefs = b.getPreferencesAsArray();
        
        if( weights.size() != aPrefs.size() || weights.size() != bPrefs.size() ) {
        	System.out.println(weights.toString() + "\n" + aPrefs.toString() + "\n" + bPrefs.toString());
        	throw new IllegalArgumentException("A major error occurred, arrays do not align.\n"
					+ "Thrown in MatchCalculator.java");
        }
        
        for(int i=0; i<aPrefs.size(); i++) {
        	s = weights.get(i).split(" ");
        	
        	if(aPrefs.get(i) != null && bPrefs.get(i) != null && 
        			aPrefs.get(i).equalsIgnoreCase(bPrefs.get(i))) {
        		score += Integer.parseInt(s[0]);
        	} else {
        		score -= Integer.parseInt(s[1]);
        	}
        }
        
        /*
        // cleanliness weighted highest based on student client feedback
        if (a.getCleanliness().equalsIgnoreCase(b.getCleanliness())) {
            score += 15;
        } else {
            score -= 10;
        }

        if (a.getSleepSchedule().equalsIgnoreCase(b.getSleepSchedule())) {
            score += 10;
        } else {
            score -= 5;
        }

        if (a.getGuests().equalsIgnoreCase(b.getGuests())) {
            score += 10;
        } else {
            score -= 5;
        }*/

        return score;
    }
}
