package library.ui.dashboard;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.database.handler.DatabaseHandler;

public class Dashboard extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("/library/ui/dashboard/dashboard.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Resources/StyleSheets/LibraryStyleSheet.css").toExternalForm());
			primaryStage.setTitle("Dashboard");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
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
