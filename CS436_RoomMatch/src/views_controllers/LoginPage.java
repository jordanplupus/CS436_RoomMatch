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
	private String passwordInput = "";
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
		
		// this lambda function is called when the text field changes. 
		// observable = text field being observed
		// https://docs.oracle.com/javafx/2/api/javafx/scene/control/TextInputControl.TextProperty.html
		passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {			
			// Mask the textField with '*' characters
			if( oldValue.length() < newValue.length() ) {
				passwordInput += "" + newValue.charAt(newValue.length() - 1);
				passwordTextField.setText(newValue.substring(0, newValue.length()-1) + "*");	// will call this function again
			}
			else if( oldValue.length() > newValue.length() ){ // on backspace entered
				passwordInput = passwordInput.substring(0, passwordInput.length()-1);
			}
			
			//System.out.println("passwordTextField changed from " + oldValue + " to " + newValue);
		});
		
		EventHandler<ActionEvent> handleRegistration = new RegisterHandler();
		register.setOnAction(handleRegistration);
		
		return window;
	}
	
	private class LoginHandler implements EventHandler<ActionEvent> {
		private int counter;
		@Override
		public void handle(ActionEvent arg0) {
			boolean success = controller.attemptLogin(
				usernameTextField.getText(),
				passwordInput
			);

			if (!success) {
				information.setText("Invalid username or password, please try again\n"
						+ "Login attempts: " + ++counter);
				passwordTextField.setText("");
				passwordInput = "";
			}
		}
	}
	
	private class RegisterHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			RegisterPage registerPage = new RegisterPage(controller);
			BorderPane window = registerPage.initializePanel();
			controller.setToPage(window, -1, -1);
		}
	}
}


