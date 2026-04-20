package views_controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.UserProfile;

public class PrefCard {
	
	@FXML 
	private Label prefLabel;
	@FXML
	private ComboBox<String> options;
	@FXML 
	private CheckBox dealBreaker;
	
	public void createPrefCard(String pLabel, String[] list) {
		prefLabel.setText(pLabel);
		options.setItems(FXCollections.observableArrayList(list));
	}
	
	public void setValues(String userPref, boolean userBreaker) {
		options.setValue(userPref);
		dealBreaker.setSelected(userBreaker);
		
	}
	public String getPrefOption() {
		return options.getValue();
	}
	
	public boolean getDealBreakerValue() {
		return this.dealBreaker.isSelected();
	}
	
}
