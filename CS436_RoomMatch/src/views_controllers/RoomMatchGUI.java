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
 * <p>Main is contained in this method.</p>
 */
public class RoomMatchGUI extends Application {	// TODO: Implement loops in the database and userProfile for the preferences
	public static void main(String[] args) {
		launch(args);
	}
	
	private int currentUserId = -1;

	private DatabaseManager db = new DatabaseManager();
	private UserProfile userProfile;

	private BorderPane window;
	private Stage stage;

	// Colors:
	/* Red:  			#ED593B
	 * Beige/Light:		#FFEDCF
	 * Beige/Yellow:  	#FFDEA5
	 * Pink-ish: 		#FFDDCF
	 * Brown:			#863030
	 * 
	 */

	@Override
	public void start(Stage stage) throws Exception {
		//Initialize database
		db.init();
		// Add admin account
		db.seedAdmin();
		// Show what's in the db
		db.printAllData();

		
		//db.addPreferenceEntry("major");
		//db.removePreferenceEntry("major");
		
		this.stage = stage;
		window = new BorderPane();
		Scene scene = new Scene(window);

		userProfile = new UserProfile();
		userProfile.verifyPreferenceCount(db.getPreferenceTableEntryCount() - 1);
		
		this.setToPage(View.LOGIN, "Login");

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

	public void setToPage(View fxml, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml.getFilename()));
		Parent root = loader.load();
		
		Page controller = loader.getController();
		controller.setMainController(this, userProfile);
		
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
				loadDealbreakers();
				this.setToPage(View.MAIN, "Welcome");
//				main.setInfo();
//				MainPageView mainPage = (MainPageView) this.setToPage("MainView.fxml", "Welcome");
//				mainPage.setInfo();

			} else {
				this.setToPage(View.PREF, "Set Preferences");
//				stage.setTitle("Set Preferences");
//				PreferencePage preferencePage = new PreferencePage(this, userProfile);
//				setToPage(preferencePage.initializePanel(), -1, -1);

			}
			return true;
		}
		return false;
	}

	void logout() {
		currentUserId = -1;
		userProfile.logout();
	}

	boolean register(String username, String password) throws IOException {
		if (db.insert(username, password)) {
			currentUserId = db.getUserId(username);
			stage.setWidth(500);
			stage.setHeight(350);
			stage.setTitle("Set Preferences");

			userProfile.login(username);

			this.setToPage(View.PREF, "Set Preferences");
//			PreferencePage preferencePage = new PreferencePage(this, userProfile);
//			setToPage(preferencePage.initializePanel(), -1, -1);
			return true;
		}
		return false;
	}
	
	public boolean isAdmin() {
		return db.isAdmin(currentUserId);
	}

//	public void savePreferences(String sleep, String cleanliness, String guests) {
//		db.savePreferences(currentUserId, sleep, cleanliness, guests);
//	}
	
	public void savePreferences(java.util.List<String> preferences) {
		//db.savePreferences(currentUserId, preferences.get(0), preferences.get(1), preferences.get(2));
		db.savePreferences(currentUserId, preferences);
	}

	public void getPreferences() {
		java.util.List<String> preferences = db.getPreferences(currentUserId);
		userProfile.setPreferences(preferences);
	}

	public void saveDealbreakers(boolean sleep, boolean cleanliness, boolean guests) {
	    db.saveDealbreakers(currentUserId, sleep, cleanliness, guests);
	}
	
	public void saveDealbreakers(java.util.List<Boolean> dealbreakers) {
		//db.saveDealbreakers(currentUserId, dealbreakers.get(0), dealbreakers.get(1), dealbreakers.get(2));
		db.saveDealbreakers(currentUserId, dealbreakers);
	}

	public void loadDealbreakers() {
	    boolean[] dbs = db.getDealbreakers(currentUserId);
	    userProfile.setDealbreakers(dbs[0], dbs[1], dbs[2]);
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
