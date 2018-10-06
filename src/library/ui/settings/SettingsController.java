package library.ui.settings;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable {
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
    private JFXTextField txfFinePerDay;

    @FXML
    private JFXPasswordField txfPassword;

    @FXML
    private JFXTextField txfnum_DaysWithoutFine;

    @FXML
    private JFXTextField txfUserName;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initValues();
	}
	
	@FXML
	public void SaveButton_Click(ActionEvent event) {
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
	public void CancelButton_click(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}

	private void initValues() {
		Preferences preferences = Preferences.getPreferences();
		txfnum_DaysWithoutFine.setText(String.valueOf(preferences.getnum_DaysWithoutFine()));
		txfFinePerDay.setText(String.valueOf(preferences.getFinePerDay()));
		txfUserName.setText(String.valueOf(preferences.getUsername()));
		txfPassword.setText(String.valueOf(preferences.getPassword()));
	}
}
