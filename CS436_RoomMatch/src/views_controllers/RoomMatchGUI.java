package views_controllers;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.UserProfile;
import model.DatabaseManager;

/**
 * <p>Main is contained in this method.</p>
 */
public class RoomMatchGUI extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	private int currentUserId = -1;

	private DatabaseManager db = new DatabaseManager();
	private UserProfile userProfile;
	
	private BorderPane window;
	private Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		
		
		//Initialize database
		db.init();
		// Add admin account
		db.seedAdmin();
		// Show what's in the db
		db.printAllData();
		
		
		
		this.stage = stage;
		stage.setTitle("Login");
		window = new BorderPane();
		Scene scene = new Scene(window, 300, 200);
		
		userProfile = new UserProfile();
		
		LoginPage loginPage = new LoginPage(this);
		window.setCenter(loginPage.initializePanel());
		
		stage.setScene(scene);
		stage.show();
	}
	
	public void setToPage(BorderPane page, int pageWidth, int pageHeight) {
		if( pageWidth > 10 )
			stage.setWidth(pageWidth);
		if( pageHeight > 10 )
			stage.setHeight(pageHeight);
		window.setCenter(page);
	}
	
	// Temporary login logic for Iteration 1 using hardcoded credentials.
	// This will be replaced with SQLite-based authentication in future iterations.
	boolean attemptLogin(String username, String password) {
		if (db.isValid(username, password)) {
			currentUserId = db.getUserId(username);
			stage.setWidth(500);
			stage.setHeight(350);
			stage.setTitle("Set Preferences");
			userProfile.login(username);
			
			if (!db.getPreferences(currentUserId).isEmpty()) {
				MainPageView mainPage = new MainPageView(this, userProfile);
				setToPage(mainPage.initializePanel(), 500, 400);
			} else {
				PreferencePage preferencePage = new PreferencePage(this, userProfile);
				setToPage(preferencePage.initializePanel(), -1, -1);
				
			}
			return true;
		}
		return false;
	}
	
	void logout() {
		currentUserId = -1;
		userProfile.logout();
	}
	
	void register(String username, String password) {
		if (db.insert(username, password)) {
			currentUserId = db.getUserId(username);
			stage.setWidth(500);
			stage.setHeight(350);
			stage.setTitle("Set Preferences");

			userProfile.login(username);
			
			PreferencePage preferencePage = new PreferencePage(this, userProfile);
			setToPage(preferencePage.initializePanel(), -1, -1);

		}
		
	}

	public void savePreferences(String sleep, String cleanliness, String guests) {
		db.savePreferences(currentUserId, sleep, cleanliness, guests);
	}

	public void getPreferences() {
		java.util.List<String> preferences = db.getPreferences(currentUserId);
		userProfile.setPreferences(preferences);
	}
	
	public java.util.List<model.SortProfiles> getMatches() {
    	return db.getAllMatches(currentUserId);
	}

	public int getCurrentUserId() {
		return currentUserId;
	}
	
	void deleteAccount() {
		db.delete(currentUserId);
	}
}
