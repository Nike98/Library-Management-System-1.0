package library.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.alert.ThrowAlert;
import library.export.pdf.ListToPDF;
import library.ui.settings.Preferences;

public class LibraryUtil {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
	
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
	
	public static void initPDFExport (StackPane rootPane, Node contentPane, Stage stage, List<List> data, String object) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save as PDF");
		FileChooser.ExtensionFilter extensionFilter = 
				new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extensionFilter);
		File saveLocation = fileChooser.showSaveDialog(stage);
		ListToPDF ltp = new ListToPDF();
		boolean flag = ltp.printToPDF(data, saveLocation, ListToPDF.Orientation.LANDSCAPE);
		JFXButton btnDone = new JFXButton("Done");
		JFXButton btnView = new JFXButton("View File");
		btnView.setOnAction((ActionEvent event) -> {
			try {
				Desktop.getDesktop().open(saveLocation);
			} catch (Exception ex) {
				ThrowAlert.showErrorMessage("Error occured", "Unable to load file");
			}
		});
		if (flag)
			ThrowAlert.showDialog(rootPane, contentPane, Arrays.asList(btnDone, btnView), 
					"Completed", object + " data has been exported");
	}
	
	public static Double getFineAmount(int days) {
		Preferences pref = Preferences.getPreferences();
		Integer fineDays = days - pref.getnum_DaysWithoutFine();
		double fine = 0;
		if (fineDays > 0)
			fine = fineDays * pref.getFinePerDay();

		return fine;
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
	
	public static void openFileWithDesktop(File file) {
		try {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
