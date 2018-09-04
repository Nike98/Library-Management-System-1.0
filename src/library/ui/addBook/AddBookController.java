package library.ui.addBook;

import java.net.URL;
import java.util.ResourceBundle;
import library.database.handler.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AddBookController implements Initializable {
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private JFXTextField txfISBN;
	
	@FXML
	private JFXTextField txfTitle;
	
	@FXML
	private JFXTextField txfAuthor;
	
	@FXML
	private JFXTextField txfEdition;
	
	@FXML
	private JFXTextField txfPublisher;
	
	@FXML
	private JFXTextField txfPrice;
	
	@FXML
	private JFXButton btnSave;
	
	@FXML
	private JFXButton btnCancel;
	
	DatabaseHandler dataHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataHandler = DatabaseHandler.getInstance();
	}
	
	@FXML
	private void SaveButtonEvent(ActionEvent event) {
		
		// Local String variables for the values extracted from the input
		String isbn = txfISBN.getText();
		String title = txfTitle.getText();
		String author = txfAuthor.getText();
		String edition = txfEdition.getText();
		String publisher = txfPublisher.getText();
		String price = txfPrice.getText();
		
		/* 
		 * Check if all the fields are filled 
		 * In case if any field id empty,
		 * an alert box will pop up on the screen.
		 */
		boolean flag = isbn.isEmpty() || title.isEmpty() || author.isEmpty() ||
				edition.isEmpty() || publisher.isEmpty() || price.isEmpty();
		if (flag) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please enter in all fields");
			alert.showAndWait();
			return;
		}
		
		if (isNumber(price)) {
			// If the Price field has no characters it will convert the data into integer
			Integer.parseInt(price);
		}
		else {
			// If not then it will alert the User to do so
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Price field has Characters");
			alert.showAndWait();
			return;
		}
		
		// Main SQL Query to Add the Book into the Database
		String query = " INSERT INTO BOOK VALUES (" +
					"'" + isbn + "'," +
					"'" + title + "'," + 
					"'" + author + "'," + 
					"'" + edition + "'," + 
					"'" + publisher + "'," + 
					"" + price + "," +
					"'Y' )";
		
		// REMOVE THIS BEFORE DEPLOYMENT
		System.out.println(query);
		
		// Saving the data to the Database
		if (dataHandler.exeAction(query)) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText(null);
			alert.setContentText("Save Successfull");
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
	
	/*
	 * Method to check for any characters in the Price field
	 *  
	 * If the Price field doesn't have any any characters 
	 * in it then it returns true and the code continues
	 * regular progression.
	 *
	 */
	private boolean isNumber(String p) {
		if (p.length() < 5) {
			for (int i = 0 ; i < p.length() ; i++) {
				if (Character.isDigit(p.charAt(i)) == false)
					return false;
			}
		}
		return true;
	}
}
