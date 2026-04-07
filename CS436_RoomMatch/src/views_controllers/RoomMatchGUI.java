package views_controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.DatabaseManager;
import model.UserProfile;

/**
 * <p>
 * Main is contained in this method.
 * </p>
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

	private FXMLLoader loader;
	private Parent root;

	@Override
	public void start(Stage stage) throws Exception {
		
		// Initialize database
		db.init();
		// Add admin account
		db.seedAdmin();
		// Show what's in the db
		db.printAllData();

		
		//db.addPreferenceEntry("major");
		//db.removePreferenceEntry("major");
		
		this.stage = stage;
		stage.setTitle("Login");
		window = new BorderPane();
		Scene scene = new Scene(window);

		userProfile = new UserProfile();

		loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		root = loader.load();
		LoginPage loginPage = loader.getController();
		loginPage.setMainController(this);
		window.setCenter(root);


		userProfile.verifyPreferenceCount(db.getPreferenceTableEntryCount() - 1);
	
		
		stage.setScene(scene);
		stage.show();
	}

	public void setToPage(Parent page, int pageWidth, int pageHeight) {
		if( pageWidth > 10 )
			stage.setWidth(pageWidth);
		if( pageHeight > 10 )
			stage.setHeight(pageHeight);
		window.setCenter(page);
	}

	public void setToPage(String fxml, String title) throws IOException {
		loader = new FXMLLoader(getClass().getResource(fxml));
		root = loader.load();
		
		Object controller = loader.getController();
		
		if (controller instanceof LoginPage) {
			((LoginPage) controller).setMainController(this);
		} else if (controller instanceof MainPageView) {
			((MainPageView) controller).setMainController(this, userProfile);
		}
		stage.setTitle(title);
		window.setCenter(root);
		stage.sizeToScene();
	}

	// Temporary login logic for Iteration 1 using hardcoded credentials.
	// This will be replaced with SQLite-based authentication in future iterations.
	boolean attemptLogin(String username, String password) throws IOException {
		if (db.isValid(username, password)) {
			currentUserId = db.getUserId(username);
			stage.setWidth(500);
			stage.setHeight(350);
			userProfile.login(username);

			if (!db.getPreferences(currentUserId).isEmpty()) {
				this.setToPage("MainView.fxml", "Welcome");
				MainPageView mainPage = loader.getController();
				mainPage.setMainController(this, userProfile);
				mainPage.setInfo();

			} else {
				stage.setTitle("Set Preferences");
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

	boolean register(String username, String password) {
		if (db.insert(username, password)) {
			currentUserId = db.getUserId(username);
			stage.setWidth(500);
			stage.setHeight(350);
			stage.setTitle("Set Preferences");

			userProfile.login(username);

			PreferencePage preferencePage = new PreferencePage(this, userProfile);
			setToPage(preferencePage.initializePanel(), -1, -1);
			return true;
		}
		return false;
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
