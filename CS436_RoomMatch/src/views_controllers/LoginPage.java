package views_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class LoginPage {
	private TextField usernameTextField = new TextField();
	private TextField passwordTextField = new TextField();
	private Label information = new Label("");
	private RoomMatchGUI controller;

	public LoginPage(RoomMatchGUI source) {
		controller = source;
	}
	
	// Display this GUI panel
	BorderPane initializePanel() {
		BorderPane window = new BorderPane();
		Label username = new Label("Username");
		Label password = new Label("Password");
		Button login = new Button("Login");
		Button register = new Button("Register");
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(5);
		grid.setHgap(5);
		
		grid.add(username, 0, 0);
		grid.add(usernameTextField, 1, 0);
		grid.add(password, 0, 1);
		grid.add(passwordTextField, 1, 1);
		grid.add(login, 1, 2);
		grid.add(register, 1, 3);
		
		window.setTop(grid);
		window.setCenter(information);
		
		EventHandler<ActionEvent> handleLogin = new LoginHandler();
		login.setOnAction(handleLogin);
		usernameTextField.setOnAction(handleLogin);
		passwordTextField.setOnAction(handleLogin);
		
		EventHandler<ActionEvent> handleRegistration = new RegisterHandler();
		register.setOnAction(handleRegistration);
		
		return window;
	}
	
	private class LoginHandler implements EventHandler<ActionEvent> {
		private int counter;
		
		@Override
		public void handle(ActionEvent arg0) {
			controller.attemptLogin(usernameTextField.getText(), passwordTextField.getText());
			
			// if login fails print failure text
			information.setText("Invalid username or password, please try again\n"
					+ "Login attempts: " + ++counter);
			passwordTextField.setText("");
		}
	}
	
	private class RegisterHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			RegisterPage registerPage = new RegisterPage(controller);
			BorderPane window = registerPage.initializePanel();
			controller.setToPage(window);
		}
	}
}


