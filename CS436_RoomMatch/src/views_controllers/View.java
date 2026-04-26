package views_controllers;

public enum View {
	LOGIN("LoginView.fxml"), MAIN("MainView.fxml"), REGISTER("RegisterView.fxml"), PREF("PrefView.fxml"), 
	MATCHES("MatchCardView.fxml"), PREFCARD("PrefCardView.fxml"), ADDPREF("AddPreferenceView.fxml"), 
	REMOVEPREF("RemovePreferenceView.fxml");
	
	private String filename;
	
	View(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
}
