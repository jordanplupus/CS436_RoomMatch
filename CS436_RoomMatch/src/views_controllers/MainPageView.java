package views_controllers;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.SortProfiles;
import model.UserProfile;

public class MainPageView implements Page {
	RoomMatchGUI controller;
	UserProfile userProfile;
	private Menu options;

	@FXML
	private Label welcomeLabel;
	@FXML
	private Label sleepLabel;
	@FXML
	private Label cleanlinessLabel;
	@FXML
	private Label guestsLabel;
	@FXML
	private Label noMatches;
	@FXML
	private VBox infoBox;
	@FXML
	private ScrollPane scrollPane;
	
	
	@FXML
	private MenuItem option1;
	@FXML
	private MenuItem option2;
	@FXML
	private MenuItem option3;
	@FXML
	private MenuItem option4 = new MenuItem("Modify existing preferences");
	
	public void setMainController(RoomMatchGUI source, UserProfile user) {
		controller = source;
		userProfile = user;
		try {
			setInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private void setInfo() throws IOException {
		
		boolean isAdmin = controller.isAdmin();
		controller.getPreferences();
		controller.loadDealbreakers();

		option4.setVisible(isAdmin);


		String sleepText = "Your Sleep Schedule: " + userProfile.getSleepSchedule();
		if (userProfile.isSleepDealbreaker())
			sleepText += " (deal-breaker)";

		String cleanText = "Your Cleanliness: " + userProfile.getCleanliness();
		if (userProfile.isCleanlinessDealbreaker())
			cleanText += " (deal-breaker)";

		String guestText = "Your Guest Frequency: " + userProfile.getGuests();
		if (userProfile.isGuestsDealbreaker())
			guestText += " (deal-breaker)";

		controller.getPreferences();
		welcomeLabel.setText("Welcome " + userProfile.getUser() + "!");
		sleepLabel.setText(sleepText);
		cleanlinessLabel.setText(cleanText);
		guestsLabel.setText(guestText);

		java.util.List<SortProfiles> matches = controller.getMatches();
		infoBox.getChildren().clear();
		
		if (matches.isEmpty()) {
			noMatches.setText("No matches found. Try adjusting your deal-breakers or check back when more users have signed up.");
			infoBox.getChildren().add(noMatches);
		}

		for (SortProfiles match : matches) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(View.MATCHES.getFilename()));
			Parent root = loader.load();

			ProfileCard card = loader.getController();
			card.createCard(match);
			infoBox.getChildren().add(root);
		}
		scrollPane.setContent(infoBox);
		
	}

	@FXML
	private void optionOneHandler(ActionEvent e) throws IOException {
		controller.setToPage(View.PREF, "Set Preferences");
	}

	@FXML
	private void optionTwoHandler(ActionEvent e) throws IOException {
		Alert alert = new Alert(AlertType.WARNING,
				"Are you sure you want to delete your account?\nThis action cannot be undone.", ButtonType.YES,
				ButtonType.CANCEL);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.YES) {
			controller.deleteAccount();
			logout();
		}
	}

	@FXML
	private void optionThreeHandler(ActionEvent e) throws IOException {
		logout();
	}

	private void logout() throws IOException {
		controller.logout();
		controller.setToPage(View.LOGIN, "Login");
	}
	
	@FXML
	private void optionFourHandler(ActionEvent e) throws IOException {
		System.out.println("Not yet implemented");
	}

}