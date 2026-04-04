package views_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.UserProfile;

public class PreferencePage {
    private RoomMatchGUI controller;
    private UserProfile userProfile;

    private ComboBox<String> sleepBox = new ComboBox<>();
    private ComboBox<String> cleanBox = new ComboBox<>();
    private ComboBox<String> guestBox = new ComboBox<>();
    private Label information = new Label("");

    public PreferencePage(RoomMatchGUI source, UserProfile user) {
        controller = source;
        userProfile = user;
    }

    BorderPane initializePanel() {
        BorderPane window = new BorderPane();

        Label title = new Label("Tell Us About Your Living Habits");
        Label sleepLabel = new Label("Your Sleep Schedule");
        Label cleanLabel = new Label("Your Cleanliness Level");
        Label guestLabel = new Label("How Often You Have Guests");

        sleepBox.getItems().addAll("early", "late");
        cleanBox.getItems().addAll("low", "medium", "high");
        guestBox.getItems().addAll("rare", "sometimes", "often");

        Button saveButton = new Button("Save Preferences");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(8);

        grid.add(title, 0, 0, 2, 1);
        grid.add(sleepLabel, 0, 1);
        grid.add(sleepBox, 1, 1);
        grid.add(cleanLabel, 0, 2);
        grid.add(cleanBox, 1, 2);
        grid.add(guestLabel, 0, 3);
        grid.add(guestBox, 1, 3);
        grid.add(saveButton, 1, 4);

        window.setTop(grid);
        window.setCenter(information);

        saveButton.setOnAction(new SaveHandler());

        return window;
    }

    private class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String sleep = sleepBox.getValue();
            String clean = cleanBox.getValue();
            String guest = guestBox.getValue();

            if (sleep == null || clean == null || guest == null) {
                information.setText("Please select all preferences before saving.");
                return;
            }

            userProfile.setPreferences(sleep, clean, guest);
            controller.savePreferences(sleep, clean, guest);
            
            MainPageView mainPage = new MainPageView(controller, userProfile);
            BorderPane window = mainPage.initializePanel();
            controller.setToPage(window, 500, 400);
        }
    }
}