package library.ui.mainInfoWindow;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
	
	DatabaseHandler dbHandler;

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
		String bookIsbn = txfIsbn.getText();
		String memId = txfMemberId.getText();
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Issue Operarion");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure eyou want to Issue the Book " + lblBookName.getText() + "\n to" + lblMemName.getText() + " ?");
		
		Optional<ButtonType> response = alert.showAndWait();
		
		if (response.get() == ButtonType.OK) {
			String str = "INSERT INTO ISSUE (isbn, member_id, issue_time) values ("
					+ "'" + bookIsbn + "', "
					+ "'" + memId + "', "
					+ "to_date(sysdate, 'dd-mon-yyyy hh:mi:ss A.M.'))";
		}
	} 
	
	/*private String getBookTitle(String isbn) {
		String query = "SELECT TITLE FROM BOOK WHERE ISBN = '" + isbn + "'";
		ResultSet rs = dbHandler.exeQuery(query);
		String title = null;
		
		try {
			while(rs.next()) {
				title = rs.getString("title");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return title;
	}*/
}
