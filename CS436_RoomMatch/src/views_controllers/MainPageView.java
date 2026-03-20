package views_controllers;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.MatchCalculator;
import model.Observer;
import model.UserProfile;

public class MainPageView extends BorderPane implements Observer {
	UserProfile userProfile;
	
	public void initializePanel(UserProfile user, RoomMatchGUI controller) {
		this.setTop(null);
		this.setCenter(null);

		userProfile = user;

		MenuItem option1 = new MenuItem("option1");
		MenuItem option2 = new MenuItem("option2");
		MenuItem option3 = new MenuItem("option3");
		Menu options = new Menu("Options");
		options.getItems().addAll(option1, option2, option3);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(options);
		this.setTop(menuBar);
		
		Label welcomeLabel = new Label("Welcome " + userProfile.getUser());
		Label sleepLabel = new Label("Sleep Schedule: " + userProfile.getSleepSchedule());
		Label cleanlinessLabel = new Label("Cleanliness: " + userProfile.getCleanliness());
		Label guestsLabel = new Label("Guest Frequency: " + userProfile.getGuests());

		java.util.List<UserProfile> matches = controller.getMatches();
		VBox infoBox = new VBox(10);
		infoBox.getChildren().addAll(welcomeLabel, sleepLabel, cleanlinessLabel, guestsLabel);

		if (matches.isEmpty()) {
			infoBox.getChildren().add(new Label("No matches found yet. Check back when more users have signed up."));
		} else {
			for (UserProfile match : matches) {
				int score = MatchCalculator.calculateScore(userProfile, match);
				int maxScore = 35;
				int percentage = (int)(((double)(score + 20) / (maxScore + 20)) * 100);
				Label matchLabel = new Label(match.getUser() + " - Compatibility: " + percentage + "%");
				infoBox.getChildren().add(matchLabel);
			}
		}

		this.setCenter(infoBox);
	}

	@Override
	public void update(Object theObserved) {
		
	}
}