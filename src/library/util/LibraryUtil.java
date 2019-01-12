package library.util;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LibraryUtil {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");

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

	public static String getDateString (Date date) {
		return DATE_FORMAT.format(date);
	}
	
	public static String formatDateTimeString (Date date) {
		return DATE_TIME_FORMAT.format(date);
	}
	
	public static String formatDateTimeString (Long time) {
		return DATE_TIME_FORMAT.format(new Date(time));
	}
}
