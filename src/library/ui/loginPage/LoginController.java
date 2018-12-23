package library.ui.loginPage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.commons.codec.digest.DigestUtils;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.alert.ThrowAlert;
import library.ui.settings.Preferences;

public class LoginController implements Initializable{
	
	@FXML
	private JFXTextField txfUsername;
	
	@FXML
	private JFXPasswordField txfPassword;
	
	Preferences preferences;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		preferences = Preferences.getPreferences();
	}
	
	@FXML
	private void CancelButton(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	private void LoginButton(ActionEvent event) {
		String uname = txfUsername.getText();
		String pass = DigestUtils.shaHex(txfPassword.getText());
		
		if (uname.equals(preferences.getUsername()) && pass.equals(preferences.getPassword())) {
			closeStage();
			LoadDashboard();
		} else {
			txfUsername.getStyleClass().add("wrong-credentials");
			txfPassword.getStyleClass().add("wrong-credentials");
		}
	}
	
	private void closeStage() {
		((Stage)txfUsername.getScene().getWindow()).close();
	}
	
	private void LoadDashboard() {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/library/ui/dashboard/mainStage/mainstage.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Dashboard");
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
