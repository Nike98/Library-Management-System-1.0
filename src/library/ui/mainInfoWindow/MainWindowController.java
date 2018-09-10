package library.ui.mainInfoWindow;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
		JFXDepthManager.setDepth(hboxBook, 1);
		JFXDepthManager.setDepth(hboxMember, 1);
		
		dbHandler = DatabaseHandler.getInstance();
	}
	
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
				lblAuthor.setText("No Such Book Avialable");
				lblStatus.setText("");
			}
		}
	}
	
	@FXML
	private void LoadMemberInfo(ActionEvent event) {
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
				lblMemName.setText("No Such Member Avialable");
				lblMemEmail.setText("");
			}
		}
	}
}
