package library.ui.mainInfoWindow;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.database.handler.DatabaseHandler;

public class MainWindowController implements Initializable {
	
	@FXML
	private HBox hboxBook;
	
	@FXML
	private HBox hboxMember;
	
	@FXML
	private Text lblBookName;
	
	@FXML
	private Text lblAuthor;
	
	@FXML
	private Text lblStatus;
	
	@FXML
	private Text lblMemName;
	
	@FXML
	private Text lblMemEmail;
	
	@FXML
	private JFXTextField txfIsbn;
	
	@FXML
	private JFXTextField txfMemberId;
	
	@FXML
    private JFXTextField Ren_txfIsbn;
	
	@FXML
    private Button Ren_btnSubmitBook;
	
	@FXML
	private ListView<String> dataList;
	
	DatabaseHandler dbHandler;
	boolean isReadytoSubmit = true;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * The JFXDepthManager is used to lift the HBox a bit from the usual screen.
		 * 
		 * Here, setDepth() method takes the arguments of the 
		 * component and the level as integer.
		 */
		JFXDepthManager.setDepth(hboxBook, 1);
		JFXDepthManager.setDepth(hboxMember, 1);
		
		dbHandler = DatabaseHandler.getInstance();
	}
	
	// Associating the particular button clicks to their respective modules 
	@FXML
	private void AddBookButton (ActionEvent event) {
		LoadWindow("/library/ui/addBook/addBook.fxml", "Add New Book");
	}
	
	@FXML
	private void ListBookButton (ActionEvent event) {
		LoadWindow("/library/ui/listBook/listBook.fxml", "Book List");
	}
	
	@FXML
	private void AddMemberButton (ActionEvent event) {
		LoadWindow("/library/ui/addMember/addMember.fxml", "Add New Member");
	}
	
	@FXML
	private void ListMemberButton (ActionEvent event) {
		LoadWindow("/library/ui/listMember/listMember.fxml", "Member List");
	}
	
	private void LoadWindow (String location, String title) {
		// This is used by the above functions to call their respective modules
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(location));
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void LoadBookInfo(ActionEvent event) {
		/* This method is associated with the Book ISBN TextField.
		 * 
		 *  It searches for the ISBN within the Book Table and returns the
		 *  the requested Data within the Text Labels.
		 *  
		 *  If no Data is given then it returns "No Input Given Text" within the Text Labels.
		 *  
		 *  If no such Book Exists then it returns "No Such Book Available"
		 *  
		 *  If the Data is found then it displays it.
		 */
		String isbn = txfIsbn.getText();
		
		if (isbn.equals("")) {
			lblBookName.setText("");
			lblAuthor.setText("No Input Given");
			lblStatus.setText("");
		}
		else {
			String query = "SELECT * FROM BOOK WHERE ISBN = '" + isbn + "'";
			ResultSet rs = dbHandler.exeQuery(query);
			Boolean flag = false;
			
			try {
				while(rs.next()) {
					String bkName = rs.getString("title");
					String bkAuthor = rs.getString("author");
					String bkStatus = rs.getString("available");
					
					lblBookName.setText(bkName);
					lblAuthor.setText(bkAuthor);
					
					if(bkStatus.equals("Y"))
						lblStatus.setText("Available");
					else
						lblStatus.setText("Book Unavailable");
						
					
					flag = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if (!flag) {
				lblBookName.setText("");
				lblAuthor.setText("No Such Book Available");
				lblStatus.setText("");
			}
		}
	}
	
	@FXML
	private void LoadMemberInfo(ActionEvent event) {
		/* This method is associated with the Member ID TextField.
		 * 
		 *  It searches for the ID within the Member Table and returns the
		 *  the requested Data within the Text Labels.
		 *  
		 *  If no Data is given then it returns "No Input Given Text" within the Text Labels.
		 *  
		 *  If no such Book Exists then it returns "No Such Member Available"
		 *  
		 *  If the Data is found then it displays it.
		 */
		String id = txfMemberId.getText();
		
		if (id.equals("")) {
			lblMemName.setText("No Input Given");
			lblMemEmail.setText("");
		}
		else {
			int int_id = Integer.parseInt(id);
			System.out.println(int_id);
			String query = "SELECT * FROM MEMBER WHERE ID = '" + int_id + "'";
			ResultSet rs = dbHandler.exeQuery(query);
			Boolean flag = false;
			
			try {
				while(rs.next()) {
					String Mem_f_Name = rs.getString("fname");
					String Mem_l_Name = rs.getString("lname");
					String MemEmail = rs.getString("email_id");
					
					lblMemName.setText(Mem_f_Name + " " + Mem_l_Name);
					lblMemEmail.setText(MemEmail);
					
					flag = true;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if (!flag) {
				lblMemName.setText("No Such Member Available");
				lblMemEmail.setText("");
			}
		}
	}

	@FXML
	private void IssueOperation(ActionEvent event) {
		/*
		 * This method here performs the Issue Operation in the
		 * Issue tab and is activated on the click of the Issue Button.
		 * 
		 * When clicked, it confirms with the User if the 
		 * Operation is to be performed or not. If the User 
		 * selects Cancel then the Operation is Cancelled.
		 * 
		 * If the user confirms, Then it performs the Issue Operation
		 * in which it issues the particular book to the Selected Member.
		 * 
		 * Simultaneously it Updates the Book table to Set the Availability 
		 * of that particular book to Unavailable.
		 * 
		 * If any Error occurs at this step then the Issue operation is
		 * aborted and the message is shown to the User.
		 * 
		 */
		String bookIsbn = txfIsbn.getText();
		String memId = txfMemberId.getText();
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Issue Operarion");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure eyou want to Issue the Book " + lblBookName.getText() + "\n to " + lblMemName.getText() + " ?");
		
		Optional<ButtonType> get_response = alert.showAndWait();
		
		if (get_response.get() == ButtonType.OK) {
			String insert_qry = "INSERT INTO ISSUE (isbn, member_id) values ("
					+ "'" + bookIsbn + "', "
					+ "'" + memId + "')";
			
			String update_qry = "UPDATE BOOK SET available = 'N' where isbn = '" + bookIsbn + "'";
			
			if (dbHandler.exeAction(insert_qry) && dbHandler.exeAction(update_qry)) {
				Alert alertCompleted = new Alert(Alert.AlertType.INFORMATION);
				alertCompleted.setTitle("Success");
				alertCompleted.setHeaderText(null);
				alertCompleted.setContentText("Book Issue Operation Completed Successfully");
				alertCompleted.showAndWait();
			}
			else {
				Alert alertFailed = new Alert(Alert.AlertType.ERROR);
				alertFailed.setTitle("Failed");
				alertFailed.setHeaderText(null);
				alertFailed.setContentText("Issue Operation Failed");
				alertFailed.showAndWait();
			}
		}
		else {
			Alert alertCancelled = new Alert(Alert.AlertType.INFORMATION);
			alertCancelled.setTitle("Cancelled");
			alertCancelled.setHeaderText(null);
			alertCancelled.setContentText("Issue Operation Cancelled by User");
			alertCancelled.showAndWait();
		}
	}

	/*
	 * 
	 * Renew / Submission tab Operations and 
	 * Events Start from further on.
	 * 
	 */
	
	@FXML
	private void Ren_LoadBookInfo(ActionEvent event) {
		/*
		 * This section of the code handles the Events 
		 * fired by the JFXTextField in the Renew / Submission section.
		 * 
		 * It takes the input from the TextField as the ISBN of a Book.
		 * Checks if the given ISBN number is present in the Issue
		 * table i.e. if the Book is Issued or not.
		 * 
		 * If the Book is not present, no operation is performed.
		 * But if the ISBN is found then the Issue related information
		 * is fetched.
		 * 
		 * With this the Book information and the Issuing Member's
		 * information is fetched too.
		 * 
		 * All this information is stored in an ObservableList.
		 * This list checks the activity of the listeners.
		 * 
		 * Further then this information is displayed to the 
		 * used in the user in the ListView which is placed 
		 * under the same TextField.
		 * 
		 * Further on the isReadytoSubmit Boolean variable is
		 * used to set if the book is Issued then it can be
		 * submitted. Otherwise it is False.
		 */
		ObservableList<String> data = FXCollections.observableArrayList();
		isReadytoSubmit = false;
		
		String isbn = Ren_txfIsbn.getText();
		String query = "SELECT * FROM ISSUE WHERE ISBN = '" + isbn + "'";
		ResultSet rs = dbHandler.exeQuery(query);
		
		try {
			while(rs.next()) {
				String ren_bookIsbn = rs.getString("isbn");
				String ren_memberId = rs.getString("member_id");
				Timestamp ren_issueTime = rs.getTimestamp("issue_time");
				int ren_count = rs.getInt("day_count");
				
				data.add("Issue Date and Time : " + ren_issueTime.toGMTString());
				data.add("Day Count : " + ren_count);
				
				data.add("Book Information :-");
				query = "SELECT * FROM BOOK WHERE ISBN = '" + ren_bookIsbn + "'";
				ResultSet rs_book = dbHandler.exeQuery(query);
				while(rs_book.next()) {
					data.add("\t ISBN : " + rs_book.getString("isbn"));
					data.add("\t Title : " + rs_book.getString("title"));
					data.add("\t Author : " + rs_book.getString("author"));
					data.add("\t Edition : " + rs_book.getString("edition_number"));
					data.add("\t Publisher : " + rs_book.getString("publisher"));
					data.add("\t Price : " + rs_book.getInt("price"));
				}
				
				data.add("Member Information :-");
				query = "SELECT * FROM MEMBER WHERE ID = '" + ren_memberId + "'";
				ResultSet rs_member = dbHandler.exeQuery(query);
				while(rs_member.next()) {
					data.add("\t ID : " + rs_member.getString("id"));
					data.add("\t Name : " + rs_member.getString("fname") + " " + rs_member.getString("lname"));
					data.add("\t Mobile Number : " + rs_member.getString("mobile_no"));
					data.add("\t Email : " + rs_member.getString("email_id"));
				}
				
				isReadytoSubmit = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dataList.getItems().setAll(data);
	}
	
	@FXML
	private void Ren_SubmitBookButton(ActionEvent event) {
		/*
		 * Insert Comment Here.
		 */
		if (!isReadytoSubmit)
			return;
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Submission Operarion");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure oyu want to return the book ?");
		
		Optional<ButtonType> get_response = alert.showAndWait();
		
		if (get_response.get() == ButtonType.OK) {
			String isbn = Ren_txfIsbn.getText();
			String del_query = "DELETE FROM ISSUE WHERE ISBN = '" + isbn + "'";
			String update_query = "UPDATE BOOK SET AVAILABLE = 'Y' WHERE ISBN = '" + isbn + "'";
			
			if (dbHandler.exeAction(del_query) && dbHandler.exeAction(update_query)) {
				Alert suc_alert = new Alert(Alert.AlertType.INFORMATION);
				suc_alert.setTitle("Successful");
				suc_alert.setHeaderText(null);
				suc_alert.setContentText("Book has been submitted");
				suc_alert.showAndWait();
			}
			else {
				Alert fail_alert = new Alert(Alert.AlertType.INFORMATION);
				fail_alert.setTitle("Failed");
				fail_alert.setHeaderText(null);
				fail_alert.setContentText("Submission of book failed");
				fail_alert.showAndWait();
			}
		}
		else {
			Alert alertCancelled = new Alert(Alert.AlertType.INFORMATION);
			alertCancelled.setTitle("Cancelled");
			alertCancelled.setHeaderText(null);
			alertCancelled.setContentText("Submission Operation Cancelled by User");
			alertCancelled.showAndWait();
		}
	}
}
