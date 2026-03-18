package views_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.DatabaseManager;

public class RegisterPage {
	private TextField usernameTextField = new TextField();
	private TextField passwordTextField = new TextField();
	private Label information = new Label("");
	private RoomMatchGUI controller;
	
	public RegisterPage(RoomMatchGUI source) {
		controller = source;
	}
	
	BorderPane initializePanel() {
		BorderPane window = new BorderPane();
		Label username = new Label("Enter Username");
		Label password = new Label("Enter Password");
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
		grid.add(register, 1, 2);
		grid.add(prevPage, 1, 3);
		
		window.setTop(grid);
		window.setCenter(information);
		
		EventHandler<ActionEvent> handleRegistration = new RegisterHandler();
		register.setOnAction(handleRegistration);
		usernameTextField.setOnAction(handleRegistration);
		passwordTextField.setOnAction(handleRegistration);
		
		EventHandler<ActionEvent> returnToLogin = new ReturnHandler();
		prevPage.setOnAction(returnToLogin);
		
		return window;
	}
	
	private class RegisterHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			controller.register(usernameTextField.getText(), passwordTextField.getText());
			information.setText("Account already exists");
			passwordTextField.setText("");
		}
	}
	
	private class ReturnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			LoginPage loginPage = new LoginPage(controller);
			BorderPane window = loginPage.initializePanel();
			controller.setToPage(window);
		}
	}
}
