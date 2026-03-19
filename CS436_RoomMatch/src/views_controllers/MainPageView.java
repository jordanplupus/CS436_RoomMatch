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
	
	public void initializePanel(UserProfile user) {
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

		UserProfile sampleMatch = new UserProfile();
		sampleMatch.login("SampleRoommate");
		sampleMatch.setPreferences("late", "high", "often");

		int score = MatchCalculator.calculateScore(userProfile, sampleMatch);

		Label matchLabel = new Label("Recommended Match: " + sampleMatch.getUser());
		// Max score = 35 (15 cleanliness + 10 sleep + 10 guests)
		// Lowest possible score = -20, so shift by +20 before converting to percentage
		int maxScore = 35;
		int percentage = (int)(((double)(score + 20) / (maxScore + 20)) * 100);

		Label scoreLabel = new Label("Compatibility Score: " + percentage + "%");

		VBox infoBox = new VBox(10);
		infoBox.getChildren().addAll(
			welcomeLabel,
			sleepLabel,
			cleanlinessLabel,
			guestsLabel,
			matchLabel,
			scoreLabel
		);

		this.setCenter(infoBox);
	}

	@Override
	public void update(Object theObserved) {
		
	}
}