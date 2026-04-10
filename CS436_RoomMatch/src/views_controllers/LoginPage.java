package views_controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.UserProfile;
import javafx.fxml.FXML;

public class LoginPage implements Page{

	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private Label information;
	private RoomMatchGUI controller;
	private int counter = 0;

	
	public void setMainController(RoomMatchGUI source, UserProfile page) {
		this.controller = source;
	}

	@FXML
	private void LoginHandler(ActionEvent event) throws IOException {
		boolean success = controller.attemptLogin(usernameTextField.getText(), passwordTextField.getText());

		if (!success) {
			information.setText("Invalid username or password, please try again\n" + "Login attempts: " + ++counter);
			passwordTextField.clear();
		}
	}

	@FXML
	private void RegisterHandler(ActionEvent event) throws IOException {
		controller.setToPage(View.REGISTER, "Register");
	}

}
