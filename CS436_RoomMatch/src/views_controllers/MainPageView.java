package views_controllers;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.SortProfiles;
import model.UserProfile;

public class MainPageView {
	RoomMatchGUI controller;
	UserProfile userProfile;
	
	public MainPageView(RoomMatchGUI source, UserProfile user) {
		controller = source;
		userProfile = user;
	}
	
	public BorderPane initializePanel() {
		BorderPane window = new BorderPane();
		
		controller.getPreferences();

		MenuItem option1 = new MenuItem("set preferences");
		MenuItem option2 = new MenuItem("delete account");
		MenuItem option3 = new MenuItem("logout");
		Menu options = new Menu("Options");
		options.getItems().addAll(option1, option2, option3);
		
		option1.setOnAction((event) -> {
			PreferencePage preferencePage = new PreferencePage(controller, userProfile);
			controller.setToPage(preferencePage.initializePanel(), 500, 400);
		});
		
		option2.setOnAction((event) -> {
			Alert alert = new Alert(AlertType.WARNING, 
					"Are you sure you want to delete your account?\nThis action cannot be undone.", 
					ButtonType.YES, ButtonType.CANCEL);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.YES) {
				controller.deleteAccount();
				logout();
			}
		});
		
		option3.setOnAction((event) -> {
			logout();
		});

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(options);
		window.setTop(menuBar);
		
		Label welcomeLabel = new Label("Welcome " + userProfile.getUser());
		Label sleepLabel = new Label("Your Sleep Schedule: " + userProfile.getSleepSchedule());
		Label cleanlinessLabel = new Label("Your Cleanliness: " + userProfile.getCleanliness());
		Label guestsLabel = new Label("Your Guest Frequency: " + userProfile.getGuests());
		Label matchesTitle = new Label("Your Matches");

		VBox infoBox = new VBox(10);
		infoBox.setPadding(new Insets(15));
		infoBox.getChildren().addAll(
				welcomeLabel,
				sleepLabel,
				cleanlinessLabel,
				guestsLabel,
				new Separator(),
				matchesTitle
		);

		java.util.List<SortProfiles> matches = controller.getMatches();

		if (matches.isEmpty()) {
			infoBox.getChildren().add(
				new Label("No matches found yet. Check back when more users have signed up.")
			);
		} else {
			for (SortProfiles match : matches) {
				VBox matchCard = buildMatchCard(match);
				infoBox.getChildren().add(matchCard);
			}
		}

		window.setCenter(infoBox);
		
		return window;
	}
	
	private VBox buildMatchCard(SortProfiles match) {
		UserProfile otherProfile = match.getOtherProfile();

		int score = match.getScore();
		int maxScore = 35;
		int percentage = (int)(((double)(score + 20) / (maxScore + 20)) * 100);

		Label nameLabel = new Label("Match: " + match.getOtherUser());
		Label percentLabel = new Label("Compatibility: " + percentage + "%");

		boolean sleepMatch = userProfile.getSleepSchedule().equalsIgnoreCase(otherProfile.getSleepSchedule());
		boolean cleanMatch = userProfile.getCleanliness().equalsIgnoreCase(otherProfile.getCleanliness());
		boolean guestMatch = userProfile.getGuests().equalsIgnoreCase(otherProfile.getGuests());

		Label sleepMatchLabel = new Label(
			"Sleep Schedule: " + compareText(userProfile.getSleepSchedule(), otherProfile.getSleepSchedule(), sleepMatch)
		);

		Label cleanMatchLabel = new Label(
			"Cleanliness: " + compareText(userProfile.getCleanliness(), otherProfile.getCleanliness(), cleanMatch)
		);

		Label guestMatchLabel = new Label(
			"Guest Frequency: " + compareText(userProfile.getGuests(), otherProfile.getGuests(), guestMatch)
		);

		String warningText = buildWarningText(sleepMatch, cleanMatch, guestMatch);
		Label warningLabel = new Label(warningText);

		VBox matchCard = new VBox(5);
		matchCard.setPadding(new Insets(10));
		matchCard.setStyle(
			"-fx-border-color: black;" +
			"-fx-border-width: 1;" +
			"-fx-background-color: white;"
		);

		matchCard.getChildren().addAll(
				nameLabel,
				percentLabel,
				sleepMatchLabel,
				cleanMatchLabel,
				guestMatchLabel
		);

		if (!warningText.isEmpty()) {
			matchCard.getChildren().add(warningLabel);
		}

		return matchCard;
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
	
	private void logout() {
		controller.logout();
		LoginPage loginPage = new LoginPage(controller);
		controller.setToPage(loginPage.initializePanel(), 300, 200);
	}
}