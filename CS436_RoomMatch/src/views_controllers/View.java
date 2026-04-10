package views_controllers;

public enum View {
	LOGIN("LoginView.fxml"), MAIN("MainView.fxml"), REGISTER("RegisterView.fxml"), PREF("PLACEHOLDER"), MATCHES("MatchCardView.fxml");
	
	private String filename;
	
	View(String filename) {
		this.filename = filename;
	}
	
	public String getFilename() {
		return filename;
	}
}
