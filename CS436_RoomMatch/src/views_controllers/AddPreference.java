package views_controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.UserProfile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import model.ReadWrite;

public class AddPreference implements Page {
	private RoomMatchGUI controller;
	private UserProfile userProfile;
	
	@FXML
	private ScrollPane scrollPane;
	@FXML 
	private VBox vb;
	@FXML
	private TextArea pName;
	@FXML
	private TextArea pDescription;
	@FXML
	private TextArea pOptions;
	@FXML
	private TextField weights;
	@FXML
	private Button savePreferences;
	@FXML
	private Button goBack;
	@FXML
	private Label information;
	
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
		Scanner file1, file2, file3;
		String line2;
		
		file1 = ReadWrite.ReadFile("/txt/descriptions.txt");
		file2 = ReadWrite.ReadFile("/txt/preferences.txt");
		file3 = ReadWrite.ReadFile("/txt/categories.txt")
;		while( file1.hasNextLine() ) {
			vb.getChildren().add(new Label("Description: " + file1.nextLine()));
			line2 = "Options: ";
			String[] split = file2.nextLine().split(" ");
			for(int i=0; i<split.length; i++) {
				line2 += split[i] + (i!=split.length-1 ? ", " : "");
			}
			vb.getChildren().add(new Label(line2));
			vb.getChildren().add(new Label());
		}
		file1.close();
		file2.close();
		
		scrollPane.setContent(vb);
	}
	
	@FXML
	private void savePreference(ActionEvent event) throws IOException {
		String options[] = pOptions.getText().split("\n");
		String checkWeights[] = weights.getText().trim().split(" ");
		String preference = "";
		
		information.setText("");
		
		if( pName.getText().isBlank() || pDescription.getText().isBlank() || pOptions.getText().isBlank() ) {
			information.setText("Either the name, description, and/or option text fields are blank.");
			return;
		}
		if( options.length <= 1 ) {
			information.setText("There should be at least two options provided");
			return;
		}
		if( checkWeights.length == 1 ) {
			information.setText("The format for weights is incorrect. (correct form: 10 5)");
			return;
		} else {
			try {
				if( Integer.parseInt(checkWeights[0]) < Integer.parseInt(checkWeights[1]) ) {
					information.setText("The first weight should be smaller than the second weight. (correct form: 10 5)");
					return;
				}
			} catch(NumberFormatException e) {
				information.setText("The format for weights is incorrect. (correct form: 10 5)");
				return;
			}
		}
		
		ReadWrite.WriteFile("/txt/descriptions.txt", pDescription.getText());
		
		for(int i=0; i<options.length; i++) {
			preference += options[i].trim().replace(' ', '_') + (i!=options.length-1 ? " " : ""); 
		}
		ReadWrite.WriteFile("/txt/preferences.txt", preference);
		
		if( weights.getText().isBlank() )
			ReadWrite.WriteFile("/txt/weights.txt", "10 5");	// will be changed later
		else {
			String weight = checkWeights[0] + " " + checkWeights[1];
			ReadWrite.WriteFile("/txt/weights.txt", weight);
		}
		ReadWrite.WriteFile("/txt/categories.txt", pName.getText());
		
		controller.addPreferenceEntry(pName.getText().trim().replace(' ', '_').toLowerCase());
		
		pName.setText("");
		pDescription.setText("");
		pOptions.setText("");
		weights.setText("");
		information.setText("");
		
		controller.setToPage(View.ADDPREF, "Add preferences");
	}
	
	@FXML
	private void returnToMainPage(ActionEvent event) throws IOException {
		controller.setToPage(View.MAIN, "Welcome");
	}
}





