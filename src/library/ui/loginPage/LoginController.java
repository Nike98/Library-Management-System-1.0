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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.alert.ThrowAlert;
import library.ui.settings.Preferences;

public class LoginController implements Initializable {
	
	private Preferences preferences;
	
	@FXML
	private JFXTextField txfUsername;
	
	@FXML
	private JFXPasswordField txfPassword;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		preferences = Preferences.getPreferences();
	}
	
	@FXML
	private void CancelButton(ActionEvent event) {
		getStage().close();
	}
	
	@FXML
	private void LoginButton(ActionEvent event) {
		String uname = txfUsername.getText();
		String pass = DigestUtils.shaHex(txfPassword.getText());
		
		if (uname.equals(preferences.getUsername()) && pass.equals(preferences.getPassword())) {
			getStage().close();
			LoadDashboard();
		} else {
			txfUsername.getStyleClass().add("wrong-credentials");
			txfPassword.getStyleClass().add("wrong-credentials");
		}
	}
	
	@FXML
	private void loginKeyPressEvent(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			LoginButton(null);
	}
	
	private Stage getStage() {
		return (Stage) txfUsername.getScene().getWindow();
	}
	
	private void LoadDashboard() {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/library/ui/dashboard/mainStage/mainstage.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Dashboard");
			stage.setScene(new Scene(parent));
			stage.show();
			//Set the Stage icon
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
