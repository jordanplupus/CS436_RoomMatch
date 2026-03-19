package model;

public class MatchCalculator {

    public static int calculateScore(UserProfile a, UserProfile b) {
        int score = 0;

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
        }

        return score;
    }
}