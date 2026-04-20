package views_controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.UserProfile;

public class RegisterPage implements Page {
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordTextField;
	@FXML
	private PasswordField rePasswordTextField;
	@FXML
	private Label information;
	private RoomMatchGUI controller;

	@Override
	public void setMainController(RoomMatchGUI source, UserProfile user) {
		controller = source;

	}

	@FXML
	private void RegisterHandler(ActionEvent event) throws IOException {
		if (!passwordTextField.getText().equals(rePasswordTextField.getText())) {
			information.setText("Passwords do not match");
			passwordTextField.setText("");
			rePasswordTextField.setText("");
			return;
		} else {
			boolean success = controller.register(usernameTextField.getText(), passwordTextField.getText());

			if (!success) {
				information.setText("Account already exists");
			}
			passwordTextField.setText("");
			rePasswordTextField.setText("");
		}

	}

	@FXML
	private void ReturnHandler(ActionEvent event) throws IOException {
		controller.logout();
		controller.setToPage(View.LOGIN, "Login");
	}

}
