package library.ui.settings;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SettingsController implements Initializable {
	
	@FXML
    private JFXTextField txfFinePerDay;

    @FXML
    private JFXPasswordField txfPassword;

    @FXML
    private JFXTextField txfnum_DaysWithoutFine;

    @FXML
    private JFXTextField txfUserName;

    Preferences preferences;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initValues();
	}

	private void initValues() {
		preferences.getPreferences();
		txfnum_DaysWithoutFine.setText(String.valueOf(preferences.getnum_DaysWithoutFine()));
		txfFinePerDay.setText(String.valueOf(preferences.getFinePerDay()));
		txfUserName.setText(String.valueOf(preferences.getUsername()));
		txfPassword.setText(String.valueOf(preferences.getPassword()));
	}
}
