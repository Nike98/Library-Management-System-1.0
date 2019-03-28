package library.ui.settings;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import library.alert.ThrowAlert;
import library.database.export.DatabaseExporter;

public class SettingsController implements Initializable {
	
	@FXML
	private StackPane rootPane;
	
	@FXML
    private JFXTextField txfFinePerDay;

    @FXML
    private JFXPasswordField txfPassword;

    @FXML
    private JFXTextField txfnum_DaysWithoutFine;

    @FXML
    private JFXTextField txfUserName;
    
    @FXML
    private JFXSpinner progressSpinner;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initValues();
	}
	
	private Stage getStage() {
		return ((Stage) txfnum_DaysWithoutFine.getScene().getWindow());
	}
	
	@FXML
	private void SaveButton_Click(ActionEvent event) {
		int nonFinedDays = Integer.parseInt(txfnum_DaysWithoutFine.getText());
		double fine = Double.parseDouble(txfFinePerDay.getText());
		String uname = txfUserName.getText();
		String passwd = txfPassword.getText();
		
		Preferences preferences = Preferences.getPreferences();
		preferences.setnum_DaysWithoutFine(nonFinedDays);
		preferences.setFinePerDay(fine);
		preferences.setUsername(uname);
		preferences.setPassword(passwd);
		
		Preferences.setPreferences(preferences);
	}
	
	@FXML
	private void CancelButton_click(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void databaseExportEvent(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Select location to create Database backup");
		File selectedDir = dirChooser.showDialog(getStage());
		if (selectedDir == null)
			ThrowAlert.showDialog(rootPane, rootPane, new ArrayList<>(),
					"Export Cancelled", "No valid directory found");
		else {
			DatabaseExporter dbExporter = new DatabaseExporter(selectedDir);
			progressSpinner.visibleProperty().bind(dbExporter.runningProperty());
			new Thread(dbExporter).start();
		}
	}

	private void initValues() {
		Preferences preferences = Preferences.getPreferences();
		txfnum_DaysWithoutFine.setText(String.valueOf(preferences.getnum_DaysWithoutFine()));
		txfFinePerDay.setText(String.valueOf(preferences.getFinePerDay()));
		txfUserName.setText(String.valueOf(preferences.getUsername()));
		txfPassword.setText(String.valueOf(preferences.getPassword()));
	}
}
