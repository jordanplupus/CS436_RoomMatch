package views_controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.CheckBox;
import model.UserProfile;

public class PreferencePage {
    private RoomMatchGUI controller;
    private UserProfile userProfile;

    private ComboBox<String> sleepBox = new ComboBox<>();
    private ComboBox<String> cleanBox = new ComboBox<>();
    private ComboBox<String> guestBox = new ComboBox<>();
    private CheckBox sleepDealbreaker = new CheckBox("Deal-breaker");
    private CheckBox cleanDealbreaker = new CheckBox("Deal-breaker");
    private CheckBox guestDealbreaker = new CheckBox("Deal-breaker");
    private Label information = new Label("");

    public PreferencePage(RoomMatchGUI source, UserProfile user) {
        controller = source;
        userProfile = user;
    }

    BorderPane initializePanel() {
        BorderPane window = new BorderPane();

        Label title = new Label("Tell Us About Your Living Habits");
        Label subtitle = new Label("Check 'Deal-breaker' for any preference you will not compromise on.");
        Label sleepLabel = new Label("Your Sleep Schedule");
        Label cleanLabel = new Label("Your Cleanliness Level");
        Label guestLabel = new Label("How Often You Have Guests");

        sleepBox.getItems().addAll("early", "late");
        cleanBox.getItems().addAll("low", "medium", "high");
        guestBox.getItems().addAll("rare", "sometimes", "often");
        
        fillWithSavedValues();

        Button saveButton = new Button("Save Preferences");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(8);

        grid.add(title, 0, 0, 3, 1);
        grid.add(subtitle, 0, 1, 3, 1);
        grid.add(sleepLabel, 0, 2);
        grid.add(sleepBox, 1, 2);
        grid.add(sleepDealbreaker, 2, 2);
        grid.add(cleanLabel, 0, 3);
        grid.add(cleanBox, 1, 3);
        grid.add(cleanDealbreaker, 2, 3);
        grid.add(guestLabel, 0, 4);
        grid.add(guestBox, 1, 4);
        grid.add(guestDealbreaker, 2, 4);
        grid.add(saveButton, 1, 5);

        window.setTop(grid);
        window.setCenter(information);

        saveButton.setOnAction(new SaveHandler());

        return window;
    }
    
    private void fillWithSavedValues() {
    	String[] preferences = userProfile.getPreferencesAsArray();
    	
    	sleepBox.setValue(preferences[0]);
    	cleanBox.setValue(preferences[1]);
    	guestBox.setValue(preferences[2]);
    	
    	sleepDealbreaker.setSelected(userProfile.isSleepDealbreaker());
    	cleanDealbreaker.setSelected(userProfile.isCleanlinessDealbreaker());
    	guestDealbreaker.setSelected(userProfile.isGuestsDealbreaker());
    }

    private class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String sleep = sleepBox.getValue();
            String clean = cleanBox.getValue();
            String guest = guestBox.getValue();
            
            boolean sleepDB = sleepDealbreaker.isSelected();
            boolean cleanDB = cleanDealbreaker.isSelected();
            boolean guestDB = guestDealbreaker.isSelected();

            if (sleep == null || clean == null || guest == null) {
                information.setText("Please select all preferences before saving.");
                return;
            }

			userProfile.setPreferences(sleep, clean, guest);
			controller.savePreferences(sleep, clean, guest);
			
			userProfile.setDealbreakers(sleepDB, cleanDB, guestDB);
			controller.saveDealbreakers(sleepDB, cleanDB, guestDB);
			try {
				controller.setToPage(View.MAIN, "Welcome");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}