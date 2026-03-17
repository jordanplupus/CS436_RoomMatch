package views_controllers;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.UserProfile;
import model.Observer;

/**
 * <p>Main is contained in this method.</p>
 */
public class RoomMatchGUI extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private UserProfile userProfile;
	private MainPageView mainPage = new MainPageView();
	
	private Observer currentView;
	private Observer mainPageView;
	
	private BorderPane window;
	private Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		stage.setTitle("Login");
		window = new BorderPane();
		Scene scene = new Scene(window, 300, 200);
		
		userProfile = new UserProfile();
		
		mainPageView = mainPage;
		userProfile.addObserver(mainPageView);
		
		LoginPage loginPage = new LoginPage(this);
		window.setCenter(loginPage.initializePanel());
		
		stage.setScene(scene);
		stage.show();
	}
	
	public void setToPage(BorderPane page) {
		window.setCenter(page);
	}
	
	// Where login database will have to be accessed
	// For now just a dummy method
	// Note: no public private modifier, lowers security by one level: 
	//		 -> other methods in same package can access this method
	void attemptLogin(String username, String password) {
		if( username.equals("Smith") && password.equals("password") ) {
			stage.setWidth(500);
			stage.setHeight(500);
			stage.setTitle("User Profile");
			userProfile.login(username);
			mainPage.initializePanel(userProfile);
			setViewTo(mainPageView);
		}
	}
	
	private void setViewTo(Observer newView) {
		window.setCenter(null);
		currentView = newView;
		window.setCenter((Node) currentView);
	}
}
