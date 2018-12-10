package library.ui.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsController extends Application {

	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Resources/StyleSheets/LibraryMainStyleSheet.css").toExternalForm());
			primaryStage.setTitle("Settings");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} 
		catch(Exception e) {
			
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
