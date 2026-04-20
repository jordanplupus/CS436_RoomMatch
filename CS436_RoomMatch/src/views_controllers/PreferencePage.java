package views_controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.CheckBox;
import model.UserProfile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class PreferencePage implements Page {
	private RoomMatchGUI controller;
	private UserProfile userProfile;

	@FXML
	private ScrollPane scrollPane;
	@FXML
	private VBox infoBox;

	private ArrayList<PrefCard> preferences = new ArrayList<>();

	@Override
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
		Scanner file;
		ArrayList<String> prefLabels = new ArrayList<>();
		
		file = readFile("/txt/descriptions.txt");
		while (file.hasNextLine()) {
			String pLabel = file.nextLine();
			prefLabels.add(pLabel);
		}
		
		infoBox.getChildren().clear();
		preferences.clear();
		file = readFile("/txt/preferences.txt");
		for (int i = 0; file.hasNextLine(); i++) {
			String prefOptions[] = file.nextLine().split(" ");
			FXMLLoader loader = new FXMLLoader(getClass().getResource(View.PREFCARD.getFilename()));
			Parent root = loader.load();

			PrefCard card = loader.getController();
			card.createPrefCard(prefLabels.get(i), prefOptions);

			preferences.add(card);
			infoBox.getChildren().add(root);
		}

		fillWithSavedValues();
		scrollPane.setContent(infoBox);

	}

	private void fillWithSavedValues() {
		java.util.List<String> userPrefs = userProfile.getPreferencesAsArray();
		java.util.List<Boolean> userDeals = userProfile.getDealbreakersAsArray();

		for (int i = 0; i < this.preferences.size(); i++) {
			this.preferences.get(i).setValues(userPrefs.get(i), userDeals.get(i));
		}
	}
	
	@FXML
	private void SaveHandler(ActionEvent event) throws IOException {
		ArrayList<String> p = new ArrayList<>();
		ArrayList<Boolean> b = new ArrayList<>();
		for (int i = 0; i < this.preferences.size(); i++) {
			p.add(this.preferences.get(i).getPrefOption());
			b.add(this.preferences.get(i).getDealBreakerValue());
		}

		userProfile.setPreferences(p);
		controller.savePreferences(p);

		userProfile.setDealbreakers(b);
		controller.saveDealbreakers(b);
		controller.setToPage(View.MAIN, "Welcome");
	}

	private Scanner readFile(String path) {
		Scanner file = null;
		String workingDir = System.getProperty("user.dir");

		workingDir += path;
		try {
			file = new Scanner(new File(workingDir));
		} catch (FileNotFoundException e) {
			System.err.println("Failed to read from file " + workingDir);
			e.printStackTrace();
		}

		/*
		 * while( file.hasNextLine() ) { System.out.println(file.nextLine()); }
		 */

		return file;
	}
}