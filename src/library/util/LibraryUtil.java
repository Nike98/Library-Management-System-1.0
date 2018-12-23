package library.util;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LibraryUtil {

	/*
	 * Add the Icon setting Method here
	 */
	
	public static void LoadWindow(URL location, String title, Stage parentStage) {
		try {
			Parent parent = FXMLLoader.load(location);
			Stage stage = null;
			if (parentStage != null)
				stage = parentStage;
			else 
				stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			//Use icon method and set it here
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
