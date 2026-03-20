package views_controllers;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Observer;
import model.UserProfile;
import model.DatabaseManager;

/**
 * <p>Main is contained in this method.</p>
 */
public class RoomMatchGUI extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private DatabaseManager db = new DatabaseManager();
	private UserProfile userProfile;
	private MainPageView mainPage = new MainPageView();
	
	private Observer currentView;
	private Observer mainPageView;
	
	private BorderPane window;
	private Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		
		//Initialize database
		db.init();
		
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

	public void showMainPage() {
		stage.setWidth(500);
		stage.setHeight(400);
		stage.setTitle("User Profile");

		mainPage.initializePanel(userProfile);
		setViewTo(mainPageView);
	}
	
	// Temporary login logic for Iteration 1 using hardcoded credentials.
	// This will be replaced with SQLite-based authentication in future iterations.
	boolean attemptLogin(String username, String password) {
		if (username.equals("Smith") && password.equals("password") || db.isValid(username, password)) {

			stage.setWidth(500);
			stage.setHeight(350);
			stage.setTitle("Set Preferences");

			userProfile.login(username);

			PreferencePage preferencePage = new PreferencePage(this, userProfile);
			setToPage(preferencePage.initializePanel());

			return true;
		}
		return false;
	}
	
	void register(String username, String password) {
		if (db.insert(username, password)) {
			stage.setWidth(500);
			stage.setHeight(350);
			stage.setTitle("Set Preferences");

			userProfile.login(username);

			PreferencePage preferencePage = new PreferencePage(this, userProfile);
			setToPage(preferencePage.initializePanel());
		}
		
	}
	
	private void setViewTo(Observer newView) {
		window.setCenter(null);
		currentView = newView;
		window.setCenter((Node) currentView);
	}
}
