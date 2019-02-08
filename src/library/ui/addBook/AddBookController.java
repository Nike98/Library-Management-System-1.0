package library.ui.addBook;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.text.StyledEditorKit.BoldAction;

import library.alert.ThrowAlert;
import library.data.model.Book;
import library.database.handler.*;
import library.ui.listBook.ListBookController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AddBookController implements Initializable {
	
	@FXML
	private StackPane rootPane;
	
	@FXML
	private AnchorPane rootAnchorPane;

	@FXML
	private JFXTextField txfIsbn;
	
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
	
	private DatabaseHandler dbHandler;
	private Boolean isInEditMode = Boolean.FALSE;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dbHandler = DatabaseHandler.getInstance();
	}
	
	@FXML
	private void SaveButtonEvent(ActionEvent event) {
		
		// Local String variables for the values extracted from the input
		String isbn = txfIsbn.getText();
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
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Insufficient Data", "Please enter data in all the fields");
			return;
		}
		
		if (isInEditMode) {
			handleEditOperation();
			return;
		}
		
		if (DatabaseHelper.checkBookExistence(isbn)) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Duplicate Book ISBN", 
					"Book with the same ISBN already exists.\nPlease use a new ISBN code/number");
			return;
		}
		
		if (isNumber(price)) {
			Integer.parseInt(price);
		} else {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Input Mismatch", 
					"Price Field has characters. Please enter numerical values");
			return;
		}
		
		Book book = new Book(isbn, title, author, edition, publisher, price, Boolean.TRUE);
		boolean result = DatabaseHelper.insertNewBook(book);
		if (result) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "New Book Added", 
					title + " has been added");
			clearEntries();
		} else {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Failed to Add New Book", 
					"Please check all the entries and try again");
		}
	}
	
	@FXML
	private void CancelButtonEvent(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
	}
	
	private boolean isNumber(String price) {
		/*
		 * Method to check for any characters in the Price field
		 *  
		 * If the Price field doesn't have any any characters 
		 * in it then it returns true and the code continues
		 * regular progression.
		 *
		 */
		if (price.length() < 6) {
			for (int i = 0 ; i < price.length() ; i++) {
				if (Character.isDigit(price.charAt(i)) == false)
					return false;
			}
		}
		return true;
	}
	
	public void inflateUI(ListBookController.Book book) {
		txfIsbn.setText(book.getIsbn());
		txfIsbn.setEditable(false);
		txfTitle.setText(book.getTitle());
		txfAuthor.setText(book.getAuthor());
		txfEdition.setText(book.getEdition());
		txfPublisher.setText(book.getPublisher());
		txfPrice.setText(book.getPrice());
		isInEditMode = Boolean.TRUE;
	}
	
	private void clearEntries() {
		txfIsbn.clear();
		txfTitle.clear();
		txfAuthor.clear();
		txfEdition.clear();
		txfPublisher.clear();
		txfPrice.clear();
	}
	
	private void handleEditOperation() {
		ListBookController.Book book = new ListBookController.Book(
				txfIsbn.getText(), txfTitle.getText(), txfAuthor.getText(), txfEdition.getText(), 
				txfPublisher.getText(), txfPrice.getText(), true);
		if (dbHandler.updatebook(book))
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Operation Successfull", "Book Update Completed Successfully");
		else
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), "Operation Failed", "Book Update Failed");
	}
}
