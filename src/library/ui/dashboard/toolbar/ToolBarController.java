package library.ui.dashboard.toolbar;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import library.ui.callback.BookReturnCallback;
import library.ui.issueList.IssueListController;
import library.util.LibraryUtil;

public class ToolBarController implements Initializable{
	
	private BookReturnCallback callback;

	@FXML
	private JFXButton btnAddBook;

	@FXML
	private JFXButton btnAddMember;

	@FXML
	private JFXButton btnListBook;

	@FXML
	private JFXButton btnListMember;
	 
	@FXML
	private JFXButton btnListIssue;
	 
	@FXML
	private JFXButton btnSettings;
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
	
	public void setBookReturnCallback(BookReturnCallback callback) {
		this.callback = callback;
	}

	@FXML
	private void AddBookButton(ActionEvent event) {
		LibraryUtil.LoadWindow(getClass().getResource("/library/ui/addbook/addbook.fxml"), "Add New Book", null);
	}

	@FXML
	private void AddMemberButton(ActionEvent event) {
		LibraryUtil.LoadWindow(getClass().getResource("/library/ui/addMember/addMember.fxml"), "Add New Member", null);
	}

	@FXML
	private void ListBookButton(ActionEvent event) {
		LibraryUtil.LoadWindow(getClass().getResource("/library/ui/listBook/listBook.fxml"), "Book List", null);
	}

	@FXML
	private void ListMemberButton(ActionEvent event) {
		LibraryUtil.LoadWindow(getClass().getResource("/library/ui/listMember/listMember.fxml"), "Member List", null);
	}
	
	@FXML
	private void ListIssueButton(ActionEvent event) {
		LibraryUtil.LoadWindow(getClass().getResource("/library/ui/issueList/issueList.fxml"), "Book Issue List", null);
	}

	@FXML
	private void SettingsButton(ActionEvent event) {
		LibraryUtil.LoadWindow(getClass().getResource("/library/ui/settings/settings.fxml"), "Settings", null);
	}
}
