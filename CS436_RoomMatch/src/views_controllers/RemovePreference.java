package views_controllers;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.UserProfile;
import model.ReadWrite;

public class RemovePreference implements Page {
	private RoomMatchGUI controller;
	private UserProfile userProfile;
	private Font system = new Font("system", 20);
	
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private ComboBox<String> deleteOptions; 
	@FXML
	private VBox vb;
	@FXML
	private Button delete;
	@FXML
	private Button returnToMain;
	@FXML
	private Label information;
	
	@Override
	public void setMainController(RoomMatchGUI source, UserProfile user) {
		controller = source;
		userProfile = user;
		
		try {
			setInfo();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setInfo() throws IOException {
		java.util.List<String> descs, prefs;
		String options;
		
		deleteOptions.setItems(FXCollections.observableArrayList(getMatchDescs()));
		
		descs = ReadWrite.RetrieveFileAsTextArr("/txt/descriptions.txt");
		prefs = ReadWrite.RetrieveFileAsTextArr("/txt/preferences.txt");
		
		for(int i=0; i<descs.size(); i++) {
			Label description = new Label(descs.get(i));
			description.setFont(system);
			description.setWrapText(true);
			vb.getChildren().add(description);
			options = "Options: ";
			String split[] = prefs.get(i).split(" ");
			for(int j=0; j<split.length; j++) {
				options += split[j] + (j!=split.length-1 ? ", " : "");
			}
			Label preference = new Label(options);
			preference.setWrapText(true);
			preference.setFont(system);
			vb.getChildren().add(preference);
			vb.getChildren().add(new Label());
		}
		
		scrollPane.setContent(vb);
	}
	
	private String[] getMatchDescs() {
		java.util.List<String> matchDescs = controller.getMatchDescNames();
		String d[] = new String[matchDescs.size()-1];
		
		for(int i=1; i<matchDescs.size(); i++) {
			d[i-1] = matchDescs.get(i);
		}
		
		return d;
	}
	
	@FXML
	private void deletePreference(ActionEvent event) {
		String columnToDelete = deleteOptions.getValue();
		
		if( columnToDelete == null ) {
			information.setText("No value selected.");
			return;
		}
		
		//controller.removePreferenceEntry(columnToDelete);
		
		deleteOptions.setValue(null);
		information.setText("");
	}
	
	@FXML
	private void returnToMainPage(ActionEvent event) throws IOException {
		controller.setToPage(View.MAIN, "Welcome");
	}
}
