package views_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class RegisterPage {
	private TextField usernameTextField = new TextField();
	private TextField passwordTextField = new TextField();
	private TextField rePasswordTextField = new TextField();
	private Label information = new Label("");
	private RoomMatchGUI controller;
	private String passwordInput = "";
	private String confPasswordInput = "";
	
	public RegisterPage(RoomMatchGUI source) {
		controller = source;
	}
	
	BorderPane initializePanel() {
		BorderPane window = new BorderPane();
		Label username = new Label("Enter Username");
		Label password = new Label("Enter Password");
		Label repassword = new Label("Confirm Password");
		Button register = new Button("Register");
		Button prevPage = new Button("Cancel");
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		grid.add(username, 0, 0);
		grid.add(usernameTextField, 1, 0);
		grid.add(password, 0, 1);
		grid.add(passwordTextField, 1, 1);
		grid.add(repassword, 0, 2);
		grid.add(rePasswordTextField, 1, 2);
		grid.add(register, 1, 3);
		grid.add(prevPage, 1, 4);
		
		window.setTop(grid);
		window.setCenter(information);
		
		EventHandler<ActionEvent> handleRegistration = new RegisterHandler();
		register.setOnAction(handleRegistration);
		usernameTextField.setOnAction(handleRegistration);
		passwordTextField.setOnAction(handleRegistration);
		
		passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {			
			// Mask the textField with '*' characters
			if( oldValue.length() < newValue.length() ) {
				passwordInput += "" + newValue.charAt(newValue.length() - 1);
				passwordTextField.setText(newValue.substring(0, newValue.length()-1) + "*");	// will call this function again
			}
			else if( oldValue.length() > newValue.length() ){ // on backspace entered
				passwordInput = passwordInput.substring(0, passwordInput.length()-1);
			}
		});
		rePasswordTextField.textProperty().addListener((observable, oldValue, newValue) -> {			
			// Mask the textField with '*' characters
			if( oldValue.length() < newValue.length() ) {
				confPasswordInput += "" + newValue.charAt(newValue.length() - 1);
				rePasswordTextField.setText(newValue.substring(0, newValue.length()-1) + "*");	// will call this function again
			}
			else if( oldValue.length() > newValue.length() ){ // on backspace entered
				confPasswordInput = confPasswordInput.substring(0, confPasswordInput.length()-1);
			}
		});
		
		EventHandler<ActionEvent> returnToLogin = new ReturnHandler();
		prevPage.setOnAction(returnToLogin);
		
		return window;
	}
	
	private class RegisterHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {			
			if( !passwordInput.equals(confPasswordInput) ) {
				information.setText("Passwords do not match");
				passwordTextField.setText("");
				passwordInput = "";
				rePasswordTextField.setText("");
				confPasswordInput = "";
				return;
			}
			
			controller.register(usernameTextField.getText(), passwordInput);
			information.setText("Account already exists");
			passwordTextField.setText("");
			passwordInput = "";
			rePasswordTextField.setText("");
			confPasswordInput = "";
		}
	}
	
	private class ReturnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			LoginPage loginPage = new LoginPage(controller);
			BorderPane window = loginPage.initializePanel();
			controller.setToPage(window, -1, -1);
		}
	}
}
