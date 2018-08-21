package library.ui.mainInfoWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Resources/Stylesheet/MainStyleSheet.css").toExternalForm());
			primaryStage.setTitle("");
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
