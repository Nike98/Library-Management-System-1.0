package library.ui.addMember;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.alert.ThrowAlert;
import library.database.handler.DatabaseHandler;
import library.database.handler.DatabaseHelper;
import library.ui.listMember.ListMemberController;
import library.ui.listMember.ListMemberController.Member;

public class AddMemberController implements Initializable{
	
	@FXML
	private StackPane rootPane;
	
	@FXML
	private AnchorPane rootAnchorPane;

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
	
	private DatabaseHandler dbHandler;
	private Boolean isInEditMode = false;

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
		Boolean flag = memName.isEmpty() || memCity.isEmpty()
				|| memAddress.isEmpty() || memMobile.isEmpty() || memEmail.isEmpty();
		if (flag) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Insufficient Data", "Please enter data in all the fields");
			return;
		}
		
		if (isInEditMode) {
			handleUpdateMember();
			return;
		}
		
		boolean result = DatabaseHelper.insertNewMember(memName, memCity, memAddress, memMobile, memEmail);
		if (result) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"New member added", "New Member " + memName + " has been added");
			clearEntries();
			Stage stage = (Stage) rootPane.getScene().getWindow();
			stage.close();
		} else 
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"Failed to add new member", "Please re-check your entries and try again");
	}
	
	@FXML
	private void CancelButtonEvent(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
	
	private void clearEntries() {
		txfName.clear();
		txfCity.clear();
		txfAddress.clear();
		txfMobileNo.clear();
		txfEmail.clear();
	}
	
	private void handleUpdateMember() {
		Member member = new ListMemberController.Member(
				null, txfName.getText(), txfCity.getText(), txfAddress.getText(), txfMobileNo.getText(), txfEmail.getText());
		if (dbHandler.updateMember(member))
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Success", "Member data updated");
		else
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Failed", "Member update operation failed");
	}
}
