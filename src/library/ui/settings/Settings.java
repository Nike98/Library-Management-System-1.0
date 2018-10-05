package library.ui.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.database.handler.DatabaseHandler;

public class Settings extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Resources/Stylesheet/MainStyleSheet.css").toExternalForm());
			primaryStage.setTitle("Settings");
			primaryStage.setScene(scene);
			primaryStage.show();
			
			// This Decreases the time the Application takes to load
			new Thread(new Runnable() {
				@Override
				public void run() {
					DatabaseHandler.getInstance();
				}
			}).start();
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
