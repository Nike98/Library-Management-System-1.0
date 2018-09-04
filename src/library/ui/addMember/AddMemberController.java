package library.ui.addMember;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.database.handler.DatabaseHandler;

public class AddMemberController implements Initializable{
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXTextField txfFirstName;
	
	@FXML
	private JFXTextField txfLastName;
	
	@FXML
	private JFXTextField txfCity;
	
	@FXML
	private JFXTextArea txfAddress;
	
	@FXML
	private JFXTextField txfMobileNo;
	
	@FXML
	private JFXTextField txfEmail;
	
	@FXML
	private JFXButton btnAdd;
	
	@FXML
	private JFXButton btnCancel;
	
	DatabaseHandler dbHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbHandler = DatabaseHandler.getInstance();
	}

	@FXML
	private void AddButtonEvent(ActionEvent event) {
		String memFname = txfFirstName.getText();
		String memLname = txfLastName.getText();
		String memCity = txfCity.getText();
		String memAddress = txfAddress.getText();
		String memMobile = txfMobileNo.getText();
		String memEmail = txfEmail.getText();
		
		/* 
		 * Check if all the fields are filled 
		 * In case if any field id empty,
		 * an alert box will pop up on the screen.
		 */
		boolean flag = memFname.isEmpty() || memLname.isEmpty() || memCity.isEmpty()
				|| memAddress.isEmpty() || memMobile.isEmpty() || memEmail.isEmpty();
		if (flag) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please enter in all fields");
			alert.showAndWait();
			return;
		}
		
		// Main SQL Query to Add the Book into the Database
		String query = " INSERT INTO MEMBER VALUES (" +
					"member_sequence.nextval," +
					"'" + memFname + "'," +
					"'" + memLname + "'," + 
					"'" + memCity + "'," + 
					"'" + memAddress + "'," + 
					"'" + memMobile + "'," + 
					"'" + memEmail + "'" +
					")";
				
		// REMOVE THIS BEFORE DEPLOYMENT
		System.out.println(query);
		
		// Saving the data to the Database
		if (dbHandler.exeAction(query)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Member Added Successfully");
			alert.showAndWait();
		}
		else {
			// On Error
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Failed to Save to Database");
			alert.showAndWait();
		}
	}
	
	@FXML
	private void CancelButtonEvent(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
}
