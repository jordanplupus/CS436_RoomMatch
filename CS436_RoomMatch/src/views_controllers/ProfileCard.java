package views_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.SortProfiles;
import model.UserProfile;

public class ProfileCard {
	@FXML
	private Label nameLabel;
	@FXML
	private Label percentLabel;
	@FXML
	private Label sleepMatchLabel;
	@FXML
	private Label cleanMatchLabel;
	@FXML
	private Label guestMatchLabel;
	@FXML
	private Label warningLabel;

	public void createCard(SortProfiles match) {
		UserProfile otherProfile = match.getOtherProfile();
		UserProfile userProfile = match.getUserProfile();
		int score = match.getScore();
		int maxScore = 35;
		int percentage = (int) (((double) (score + 20) / (maxScore + 20)) * 100);

		nameLabel.setText("Match: " + match.getOtherUser());
		percentLabel.setText("Compatability: " + percentage + "%");

		boolean sleepMatch = userProfile.getSleepSchedule().equalsIgnoreCase(otherProfile.getSleepSchedule());
		boolean cleanMatch = userProfile.getCleanliness().equalsIgnoreCase(otherProfile.getCleanliness());
		boolean guestMatch = userProfile.getGuests().equalsIgnoreCase(otherProfile.getGuests());

		sleepMatchLabel.setText("Sleep Schedule: "
				+ compareText(userProfile.getSleepSchedule(), otherProfile.getSleepSchedule(), sleepMatch));
		cleanMatchLabel.setText(
				"Cleanliness: " + compareText(userProfile.getCleanliness(), otherProfile.getCleanliness(), cleanMatch));
		guestMatchLabel.setText(
				"Guest Frequency: " + compareText(userProfile.getGuests(), otherProfile.getGuests(), guestMatch));

		String warningText = buildWarningText(sleepMatch, cleanMatch, guestMatch);
		warningLabel.setText(warningText);

	}

	private String compareText(String currentValue, String otherValue, boolean isMatch) {
		if (isMatch) {
			return "Match (" + currentValue + ")";
		}
		return "Mismatch (" + currentValue + " vs " + otherValue + ")";
	}

	private String buildWarningText(boolean sleepMatch, boolean cleanMatch, boolean guestMatch) {
		String warning = "";

		if (!cleanMatch) {
			warning += "Potential conflict: cleanliness. ";
		}
		if (!sleepMatch) {
			warning += "Potential conflict: sleep schedule. ";
		}
		if (!guestMatch) {
			warning += "Potential conflict: guest frequency.";
		}

		return warning.trim();
	}
}
