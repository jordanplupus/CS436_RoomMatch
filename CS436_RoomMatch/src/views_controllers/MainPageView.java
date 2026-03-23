package views_controllers;

import java.util.Comparator;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.MatchCalculator;
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
			if( result.get() == ButtonType.YES ) {
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
		Label sleepLabel = new Label("Sleep Schedule: " + userProfile.getSleepSchedule());
		Label cleanlinessLabel = new Label("Cleanliness: " + userProfile.getCleanliness());
		Label guestsLabel = new Label("Guest Frequency: " + userProfile.getGuests());

		java.util.List<SortProfiles> matches = controller.getMatches();
	
		VBox infoBox = new VBox(10);
		infoBox.getChildren().addAll(welcomeLabel, sleepLabel, cleanlinessLabel, guestsLabel);

		if (matches.isEmpty()) {
			infoBox.getChildren().add(new Label("No matches found yet. Check back when more users have signed up."));
		} else {
			for (SortProfiles match : matches) {
				int score = match.getScore();//MatchCalculator.calculateScore(userProfile, match);
				int maxScore = 35;
				int percentage = (int)(((double)(score + 20) / (maxScore + 20)) * 100);
				Label matchLabel = new Label(match.getOtherUser() + " - Compatibility: " + percentage + "%");
				infoBox.getChildren().add(matchLabel);
			}
		}

		window.setCenter(infoBox);
		
		return window;
	}
	
	private void logout() {
		controller.logout();
		LoginPage loginPage = new LoginPage(controller);
		controller.setToPage(loginPage.initializePanel(), 300, 200);
	}
}