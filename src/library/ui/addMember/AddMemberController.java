package library.ui.addMember;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.alert.ThrowAlert;
import library.database.handler.DatabaseHandler;

public class AddMemberController implements Initializable{
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXTextField txfName;
	
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
		String memName =txfName.getText();
		String memCity = txfCity.getText();
		String memAddress = txfAddress.getText();
		String memMobile = txfMobileNo.getText();
		String memEmail = txfEmail.getText();
		
		/* 
		 * Check if all the fields are filled 
		 * In case if any field id empty,
		 * an alert box will pop up on the screen.
		 */
		boolean flag = memName.isEmpty() || memCity.isEmpty()
				|| memAddress.isEmpty() || memMobile.isEmpty() || memEmail.isEmpty();
		if (flag) {
			ThrowAlert.showErrorMessage("Error Occured", "Please enter in all fields");
			return;
		}
		
		// Main SQL Query to Add the Book into the Database
		String query = " INSERT INTO MEMBER " +
				 	"(name, city, address, mobile_no, email_id)" +
					" VALUES (" +
					"'" + memName + "'," + 
					"'" + memCity + "'," + 
					"'" + memAddress + "'," + 
					"" + memMobile + "," + 
					"'" + memEmail + "'" +
					")";
				
		// REMOVE THIS BEFORE DEPLOYMENT
		System.out.println(query);
		
		// Saving the data to the Database
		if (dbHandler.exeAction(query)) {
			ThrowAlert.showInformationMessage("Operation Successful", "Member Added Successfully");
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.close();
		} else {
			// On Error
			ThrowAlert.showErrorMessage("Error Occured", "Failed to Save to Database");
		}
	}
	
	@FXML
	private void CancelButtonEvent(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
}
