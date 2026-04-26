package views_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.SortProfiles;
import model.UserProfile;

public class ProfileCard {
	private Font system = new Font("system", 20);
	
	@FXML
	private Label nameLabel;
	@FXML
	private Label percentLabel;
	/*
	@FXML
	private Label sleepMatchLabel;
	@FXML
	private Label cleanMatchLabel;
	@FXML
	private Label guestMatchLabel;
	@FXML
	private Label warningLabel;
	*/
	@FXML
	private VBox labels;

	public void createCard(SortProfiles match, java.util.List<String> descr) {
		UserProfile otherProfile = match.getOtherProfile();
		UserProfile userProfile = match.getUserProfile();
		java.util.List<String> userPrefs = userProfile.getPreferencesAsArray();
		java.util.List<String> otherPrefs = otherProfile.getPreferencesAsArray();
		boolean matches[] = new boolean[userPrefs.size()];
		int score = match.getScore();
		int maxScore = 35;
		int percentage = (int) (((double) (score + 20) / (maxScore + 20)) * 100);

		nameLabel.setText("Match: " + match.getOtherUser());
		percentLabel.setText("Compatability: " + percentage + "%");

		//labels.getChildren().add(cleanMatchLabel);
		
		for(int i=0; i<userPrefs.size(); i++) {
			if( userPrefs.get(i) != null ) 
				matches[i] = userPrefs.get(i).equalsIgnoreCase(otherPrefs.get(i));
			else matches[i] = false;
		}
		/*
		boolean sleepMatch = userProfile.getSleepSchedule().equalsIgnoreCase(otherProfile.getSleepSchedule());
		boolean cleanMatch = userProfile.getCleanliness().equalsIgnoreCase(otherProfile.getCleanliness());
		boolean guestMatch = userProfile.getGuests().equalsIgnoreCase(otherProfile.getGuests());
		*/
		
		for(int i=0; i<userPrefs.size(); i++) {
			String text = descr.get(i+1).replace('_', ' ');
			text = (char)(text.charAt(0) - 'a' + 'A') + text.substring(1);
			text += ": " + compareText(userPrefs.get(i), otherPrefs.get(i), matches[i]);
			Label toDisplay = new Label(text);
			toDisplay.setFont(system);
			labels.getChildren().add(toDisplay);
		}
		
		/*
		sleepMatchLabel.setText("Sleep Schedule: "
				+ compareText(userProfile.getSleepSchedule(), otherProfile.getSleepSchedule(), sleepMatch));
		cleanMatchLabel.setText(
				"Cleanliness: " + compareText(userProfile.getCleanliness(), otherProfile.getCleanliness(), cleanMatch));
		guestMatchLabel.setText(
				"Guest Frequency: " + compareText(userProfile.getGuests(), otherProfile.getGuests(), guestMatch));
		*/

		String warningText = buildWarningText(matches, descr);
		Label toDisplay = new Label(warningText);
		toDisplay.setFont(system);
		toDisplay.setWrapText(true);
		labels.getChildren().add(toDisplay);
		//warningLabel.setText(warningText);

	}

	private String compareText(String currentValue, String otherValue, boolean isMatch) {
		if (isMatch) {
			return "Match (" + currentValue + ")";
		}
		return "Mismatch (" + currentValue + " vs " + otherValue + ")";
	}
	
	private String buildWarningText(boolean matches[], java.util.List<String> descr) {
		String warning = "";
		
		for(int i=0; i<matches.length; i++) {
			if( !matches[i] ) {
				warning += (warning.isEmpty() ? "Potential conflict: " : ", ") + descr.get(i+1).replace('_', ' ');
			}
		}
		
		return warning;
	}

	/*
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
	}*/
}
