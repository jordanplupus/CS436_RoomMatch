package views_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.CheckBox;
import model.UserProfile;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

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
    
    private ArrayList<ComboBox<String>> preferences = new ArrayList<>();
    private ArrayList<CheckBox> dealbreakers = new ArrayList<>();

    public PreferencePage(RoomMatchGUI source, UserProfile user) {
        controller = source;
        userProfile = user;
    }

    BorderPane initializePanel() {
    	Scanner file;
    	ArrayList<Label> labels = new ArrayList<>();
    	
        BorderPane window = new BorderPane();

        Label title = new Label("Tell Us About Your Living Habits");
        Label subtitle = new Label("Check 'Deal-breaker' for any preference you will not compromise on.");
        //Label sleepLabel = new Label("Your Sleep Schedule");
        //Label cleanLabel = new Label("Your Cleanliness Level");
        //Label guestLabel = new Label("How Often You Have Guests");
        
        // Create description labels
        file = readFile("/txt/descriptions.txt");
        while( file.hasNextLine() )
        	labels.add(new Label(file.nextLine()));
        
        // Create preference dropdown menus
        file = readFile("/txt/preferences.txt");
        while( file.hasNextLine() ) {
        	String split[] = file.nextLine().split(" ");
        	
        	ComboBox<String> preference = new ComboBox<>();
        	for(int i=0; i<split.length; i++) {
        		preference.getItems().add(split[i]);
        	}
        	preferences.add(preference);
        }
        
        // Create dealbreaker booleans
        for(int i=0; i<preferences.size(); i++) {
        	dealbreakers.add(new CheckBox("Deal-breaker"));
        }

        /*
        sleepBox.getItems().addAll("early", "late");
        cleanBox.getItems().addAll("low", "medium", "high");
        guestBox.getItems().addAll("rare", "sometimes", "often");
        */
        
        fillWithSavedValues();

        Button saveButton = new Button("Save Preferences");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(8);

        grid.add(title, 0, 0, 3, 1);
        grid.add(subtitle, 0, 1, 3, 1);
        
        for(int i=0, j=2; i<preferences.size(); i++, j++) {
        	grid.add(labels.get(i), 0, j);
        	grid.add(preferences.get(i), 1, j);
        	grid.add(dealbreakers.get(i), 2, j);
        }
        /*
        grid.add(labels.get(0), 0, 2);
        grid.add(preferences.get(0), 1, 2);
        grid.add(sleepDealbreaker, 2, 2);
        grid.add(labels.get(1), 0, 3);
        grid.add(preferences.get(1), 1, 3);
        grid.add(cleanDealbreaker, 2, 3);
        grid.add(labels.get(2), 0, 4);
        grid.add(preferences.get(2), 1, 4);
        grid.add(guestDealbreaker, 2, 4);
        */
        
        grid.add(saveButton, 1, 5);

        window.setTop(grid);
        window.setCenter(information);

        saveButton.setOnAction(new SaveHandler());

        return window;
    }
    
    private void fillWithSavedValues() {
    	java.util.List<String> preferences = userProfile.getPreferencesAsArray();
    	java.util.List<Boolean> dealbreakers = userProfile.getDealbreakersAsArray();
    	
    	for(int i=0; i<this.preferences.size(); i++) {
    		this.preferences.get(i).setValue(preferences.get(i));
    		this.dealbreakers.get(i).setSelected(dealbreakers.get(i));
    	}
    	/*
    	this.preferences.get(0).setValue(preferences[0]);
    	this.preferences.get(1).setValue(preferences[1]);
    	this.preferences.get(2).setValue(preferences[2]);
    	
    	sleepDealbreaker.setSelected(userProfile.isSleepDealbreaker());
    	cleanDealbreaker.setSelected(userProfile.isCleanlinessDealbreaker());
    	guestDealbreaker.setSelected(userProfile.isGuestsDealbreaker());
    	*/
    }

    private class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
        	/*
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
            */
        	
        	ArrayList<String> p = new ArrayList<>();
        	ArrayList<Boolean> b = new ArrayList<>();
        	for(int i=0; i<preferences.size(); i++) {
        		p.add(preferences.get(i).getValue());
        		b.add(dealbreakers.get(i).isSelected());
        	}
        	
        	userProfile.setPreferences(p);
        	controller.savePreferences(p);
        	
        	userProfile.setDealbreakers(b);
        	controller.saveDealbreakers(b);
        	
        	/*
            userProfile.setPreferences(sleep, clean, guest);
            controller.savePreferences(sleep, clean, guest);
            
            userProfile.setDealbreakers(sleepDB, cleanDB, guestDB);
            controller.saveDealbreakers(sleepDB, cleanDB, guestDB);
            */
            
            MainPageView mainPage = new MainPageView(controller, userProfile);
            BorderPane window = mainPage.initializePanel();
            controller.setToPage(window, 500, 400);
        }
    }
    
    private Scanner readFile(String path) {
    	Scanner file = null;
    	String workingDir = System.getProperty("user.dir");
    	
    	workingDir += path;
    	try {
			file = new Scanner( new File(workingDir) );
		} catch (FileNotFoundException e) {
			System.err.println("Failed to read from file " + workingDir);
			e.printStackTrace();
		}
    	
    	/*
    	while( file.hasNextLine() ) {
    		System.out.println(file.nextLine());
    	}*/
    	
    	return file;
    }
}




