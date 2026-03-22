package views_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.MatchCalculator;
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
		
		EventHandler<ActionEvent> preferencesOption = new HandleSetPreferencesOption();
		option1.setOnAction(preferencesOption);
		
		EventHandler<ActionEvent> deleteAccountOption = new DeleteAccountOption();
		option2.setOnAction(deleteAccountOption);
		
		EventHandler<ActionEvent> logoutOption = new LogoutOption();
		option3.setOnAction(logoutOption);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(options);
		window.setTop(menuBar);
		
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

		window.setCenter(infoBox);
		
		return window;
	}
	
	private class HandleSetPreferencesOption implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			PreferencePage preferencePage = new PreferencePage(controller, userProfile);
			BorderPane window = preferencePage.initializePanel();
			controller.setToPage(window, 500, 400);
		}
	}
	
	private class DeleteAccountOption implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			controller.deleteAccount();
			logout();
		}
	}
	
	private class LogoutOption implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			logout();
		}
	}
	
	private void logout() {
		controller.logout();
		LoginPage loginPage = new LoginPage(controller);
		controller.setToPage(loginPage.initializePanel(), 300, 200);
	}
}