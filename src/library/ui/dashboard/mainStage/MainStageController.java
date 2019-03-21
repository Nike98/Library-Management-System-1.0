package library.ui.dashboard.mainStage;

import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import library.alert.ThrowAlert;
import library.database.handler.DatabaseHandler;
import library.database.handler.DatabaseHelper;
import library.ui.callback.BookReturnCallback;
import library.util.LibraryUtil;

public class MainStageController implements Initializable, BookReturnCallback {
	
	private static final String BOOK_AVAILABLE = "Book Available";
	private static final String BOOK_NOT_AVAILABLE = "Not Available";
	private static final String NO_SUCH_BOOK_AVAILABLE = "No Such Book Available";
	private static final String NO_SUCH_MEMBER_AVAILABLE = "No Such Member Available";
	
	private DatabaseHandler dbHandler = DatabaseHandler.getInstance();
	private Boolean isReadytoSubmit = false;
	
	private PieChart bookChart;
	private PieChart memberChart;
	
	@FXML
	private StackPane rootPane;
	
	@FXML
	private JFXTabPane mainTabPane;
	
	@FXML
	private Tab IssueTab;
	
	@FXML
	private Tab RenewSubmissionTab;
	
	@FXML
	private AnchorPane rootAnchorPane;
	
	@FXML
	private HBox hboxBook;
	
	@FXML
	private HBox hboxMember;
	
	@FXML
	private StackPane BookStackPane;
	
	@FXML
	private StackPane MemberStackPane;
	
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
	private JFXButton btnIssue;
	
	@FXML
	private JFXButton Ren_btnRenewBook;
	
	@FXML
    private JFXButton Ren_btnSubmitBook;
	
	@FXML
	private ListView<String> dataList;
	
	@FXML
	private JFXHamburger hamburger;
	
	@FXML
	private JFXDrawer tool_drawer;
	
	@FXML
	private HBox RenSub_DataContainer;
	
	@FXML
	private Text BoxMember_Name;
	
	@FXML
	private Text BoxMember_Email;
	
	@FXML
	private Text BoxMember_Contact;
	
	@FXML
	private Text BoxBook_Isbn;
	
	@FXML
	private Text BoxBook_Name;
	
	@FXML
	private Text BoxBook_Author;
	
	@FXML
	private Text BoxBook_Publisher;
	
	@FXML
	private Text BoxIssue_Date;
	
	@FXML
	private Text BoxIssue_NoOfDays;
	
	@FXML
	private Text BoxIssue_Fine;
	
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
		
		// Initializes the Drawer using the Hamburger
		initDrawer();
		initGraphs();
	}
	
	private void initDrawer() {
		// Method to load a Toolbox on the click of the Hamburger icon
		try {
			VBox toolbar = FXMLLoader.load(getClass().getResource("/library/ui/dashboard/toolbar/toolbar.fxml"));
			tool_drawer.setSidePane(toolbar);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
		task.setRate(-1);
		
		hamburger.addEventFilter(MouseEvent.MOUSE_CLICKED, (Event event) -> {
			task.setRate(task.getRate() * -1);
			task.play();
			
			if (tool_drawer.isClosed()) {
				tool_drawer.open();
			} else {
				tool_drawer.close();
			}			
		});
	}
	
	private void initGraphs() {
		bookChart = new PieChart(dbHandler.getBookStatistics());
		memberChart = new PieChart(dbHandler.getMemberStatistics());
		
		BookStackPane.getChildren().add(bookChart);
		MemberStackPane.getChildren().add(memberChart);
		
		IssueTab.setOnSelectionChanged((Event event) -> {
			clearIssueTabEntries();
			if (IssueTab.isSelected())
				refreshGraphs();
		});
	}
	
	private void initComponents() {
		mainTabPane.tabMinWidthProperty().bind(rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
	}
	
	private Stage getStage() {
		return (Stage) rootPane.getScene().getWindow();
	}
	
	private void clearBookCache() {
		lblBookName.setText("");
		lblAuthor.setText("");
		lblStatus.setText("");
	}
	
	private void clearMemberCache() {
		lblMemName.setText("");
		lblMemEmail.setText("");
	}
	
	private void clearIssueTabEntries() {
		// Book
			txfIsbn.clear();
			lblBookName.setText("");
			lblAuthor.setText("");
			lblStatus.setText("");
		// End
			
		// Member
			txfMemberId.clear();
			lblMemName.setText("");
			lblMemEmail.setText("");
		// End
		
		EnableDisableGraphs(true);
	}
	
	private void clearRenewTabEntries() {
		// Member
			BoxMember_Name.setText("");
			BoxMember_Email.setText("");
			BoxMember_Contact.setText("");
		// End
			
		// Book
			BoxBook_Isbn.setText("");
			BoxBook_Name.setText("");
			BoxBook_Author.setText("");
			BoxBook_Publisher.setText("");
		// End
			
		// Issue
			BoxIssue_Date.setText("");
			BoxIssue_NoOfDays.setText("");
			BoxIssue_Fine.setText("");
		// End
			
		EnableDisableControls(false);
		RenSub_DataContainer.setOpacity(0);
	}
	
	private void refreshGraphs() {
		bookChart.setData(dbHandler.getBookStatistics());
		memberChart.setData(dbHandler.getMemberStatistics());
	}
	
	private void EnableDisableGraphs(Boolean status) {
		if (status) {
			bookChart.setOpacity(1);
			memberChart.setOpacity(1);
		} else {
			bookChart.setOpacity(0);
			memberChart.setOpacity(0);
		}
	}

	private void EnableDisableControls(Boolean enableFlag) {
		if (enableFlag) {
			Ren_btnRenewBook.setDisable(false);
			Ren_btnSubmitBook.setDisable(false);
		} else {
			Ren_btnRenewBook.setDisable(true);
			Ren_btnSubmitBook.setDisable(true);
		}
	}
	
	/*
	 * 
	 * Top Navigation Menu Operations and
	 * Events Start from further here.
	 * 
	 */
	
	/*
	 * 
	 * Issue tab Operations and Events
	 * start from further here.
	 * 
	 */
	
	private boolean checkIssueValidity() {
		txfIsbn.fireEvent(new ActionEvent());
		txfMemberId.fireEvent(new ActionEvent());
		return lblBookName.getText().isEmpty() || lblAuthor.getText().isEmpty() || lblStatus.getText().isEmpty() ||
				lblMemName.getText().isEmpty() || lblMemEmail.getText().isEmpty() ||
				lblBookName.getText().equals(NO_SUCH_BOOK_AVAILABLE) || 
				lblMemName.getText().equals(NO_SUCH_MEMBER_AVAILABLE);
	}
	
	@FXML
	private void btnIssueKeyPressEvent(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER)
			IssueOperation(null);
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
		clearBookCache();
		EnableDisableGraphs(false);
		
		String isbn = txfIsbn.getText();
		String query = "SELECT * FROM BOOK WHERE ISBN = '" + isbn + "'";
		ResultSet rs = DatabaseHelper.getBookInfoWithIssueData(isbn);
		Boolean flag = false;
		
		try {
			if (rs.next()) {
				String bkName = rs.getString("title");
				String bkAuthor = rs.getString("author");
				boolean bkStatus = rs.getBoolean("available");
				Timestamp issuedOn = rs.getTimestamp("issue_time");
				
				lblBookName.setText(bkName);
				lblAuthor.setText(bkAuthor);
				
				String status = (bkStatus) ? BOOK_AVAILABLE : String.format("Issued on %s", LibraryUtil.getDateString(new Date(issuedOn.getTime())));
				if (!bkStatus)
					lblStatus.getStyleClass().add("not-available");
				else
					lblStatus.getStyleClass().remove("not-available");
				
				lblStatus.setText(status);
				
				flag = true;
			}
				
			if (!flag)
				lblAuthor.setText(NO_SUCH_BOOK_AVAILABLE);
			else
				txfMemberId.requestFocus();
		} catch (SQLException e) {
			e.printStackTrace();
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
		clearMemberCache();
		EnableDisableGraphs(false);
		
		if (txfMemberId.equals("")) {
			lblMemName.setText(NO_SUCH_MEMBER_AVAILABLE);
			return;
		}
		
		String id = txfMemberId.getText();
		Integer int_id = Integer.parseInt(id);
		//System.out.println(int_id);
		String query = "SELECT * FROM MEMBER WHERE ID = " + int_id;
		ResultSet rs = dbHandler.executeQuery(query);
		Boolean flag = false;
		
		try {
			while(rs.next()) {
				String MemName = rs.getString("name");
				String MemEmail = rs.getString("email_id");
				
				lblMemName.setText(MemName);
				lblMemEmail.setText(MemEmail);
				
				flag = true;
			}
			
			if (!flag)
				lblMemName.setText(NO_SUCH_MEMBER_AVAILABLE);
			else
				btnIssue.requestFocus();
		} catch (SQLException e) {
			e.printStackTrace();
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
		if (checkIssueValidity()) {
			JFXButton btnOk = new JFXButton("OK. Go Back");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnOk), "Invalid Input", null);
			return;
		}
		
		if (lblAuthor.getText().equals(BOOK_NOT_AVAILABLE)) {
			JFXButton btnOk = new JFXButton("OK. Go Back");
			JFXButton btnView = new JFXButton("View Details");
			btnView.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventView) -> {
				String bookToBeLoaded = txfIsbn.getText();
				Ren_txfIsbn.setText(bookToBeLoaded);
				Ren_txfIsbn.fireEvent(new ActionEvent());
				mainTabPane.getSelectionModel().select(RenewSubmissionTab);
			});
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnOk, btnView),
					"Book Already Issued", "Can't process issue request as the book is already issued");
			return;
		}
		String bookIsbn = txfIsbn.getText();
		int memId = Integer.parseInt(txfMemberId.getText());
		
		JFXButton btnYes = new JFXButton("YES");
		btnYes.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent eventYes) -> {
			String insert_query = "INSERT INTO ISSUE (isbn, member_id) values ("
					+ "'" + bookIsbn + "', "
					+ "" + memId + ")";
			
			String update_query = "UPDATE BOOK SET available = false where isbn = '" + bookIsbn + "'";
			
			if (dbHandler.executeAction(insert_query) && dbHandler.executeAction(update_query)) {
				//ThrowAlert.showInformationMessage("Success", "Book Issue Operation Completed Successfully");
				JFXButton btnBack = new JFXButton("Done. Go Back");
				btnBack.setOnAction((actionEvent) -> {
					txfIsbn.requestFocus();
				});
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnBack), 
						"Issue Successfull", "Issued " + lblBookName.getText() + " to " + lblMemName.getText());
				refreshGraphs();
			} else {
				//ThrowAlert.showErrorMessage("Failed", "Issue Operation Failed");
				JFXButton btnCheck = new JFXButton("Go Back and Check");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnCheck), "Issue Operation Failed", null);
			}
			clearIssueTabEntries();
		});
		
		JFXButton btnNo = new JFXButton("NO");
		btnNo.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventNo) -> {
			JFXButton btnFail = new JFXButton("OK. Go Back");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnFail), "Cancelled", "Issue Operation was Cancelled");
			clearIssueTabEntries();
		});
		
		ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnYes, btnNo), "Confirm Issue", 
				"Are you sure you want to Issue the Book \"" + lblBookName.getText() + "\"\n to " + lblMemName.getText() + " ?");
	}
	
	/*
	 * 
	 * Renew / Submission tab Operations and 
	 * Events Start from further here.
	 * 
	 */
	
	@Override
	public void loadBookReturn(String bookIsbn) {
		this.Ren_txfIsbn.setText(bookIsbn);
		mainTabPane.getSelectionModel().select(RenewSubmissionTab);
		Ren_LoadBookInfo(null);
		getStage().toFront();
		if (tool_drawer.isOpened())
			tool_drawer.close();
	}
	
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
		clearRenewTabEntries();
		ObservableList<String> data = FXCollections.observableArrayList();
		isReadytoSubmit = false;
		
		try {
			String isbn = Ren_txfIsbn.getText();
			String query = "SELECT "
					+ "ISSUE.isbn, ISSUE.member_id, ISSUE.issue_time, ISSUE.day_count, \n"
					+ "MEMBER.name, MEMBER.mobile_no, MEMBER.email_id, \n"
					+ "BOOK.title, BOOK.author, BOOK.publisher, BOOK.available \n"
					+ "FROM ISSUE \n"
					+ "LEFT JOIN MEMBER \n"
					+ "ON ISSUE.member_id = MEMBER.id \n"
					+ "LEFT JOIN BOOK \n"
					+ "ON ISSUE.isbn = BOOK.isbn \n"
					+ "WHERE ISSUE.isbn = '" + isbn + "'";
			
			ResultSet rs = dbHandler.executeQuery(query);
			if (rs.next()) {
				// Member Info
					BoxMember_Name.setText(rs.getString("name"));
					BoxMember_Email.setText(rs.getString("email_id"));
					BoxMember_Contact.setText(rs.getString("mobile_no"));
				// Member End
					
				// Book Info
					BoxBook_Isbn.setText(rs.getString("isbn"));
					BoxBook_Name.setText(rs.getString("title"));
					BoxBook_Author.setText(rs.getString("author"));
					BoxBook_Publisher.setText(rs.getString("publisher"));
				// Book End
					
				// Issue Info
						// Getting and Setting the date and time into proper format
							Timestamp ren_issueTime = rs.getTimestamp("issue_time");
							Date issueDate = new Date(ren_issueTime.getTime());
						//End
					BoxIssue_Date.setText(LibraryUtil.formatDateTimeString(issueDate));
						// Getting the difference of the current date and the Issued Date to Calculate the Fine (if any)
							Long timeElapsed = System.currentTimeMillis() - ren_issueTime.getTime();
							Long daysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);
						// End
					BoxIssue_NoOfDays.setText(daysElapsed.toString());
					Double fine = LibraryUtil.getFineAmount(daysElapsed.intValue());
					if (fine > 0)
						BoxIssue_Fine.setText(String.format("Fine : %f", LibraryUtil.getFineAmount(daysElapsed.intValue())));
					else
						BoxIssue_Fine.setText("");
				//Issue End
					
				isReadytoSubmit = true;
				EnableDisableControls(true);
				RenSub_DataContainer.setOpacity(1);
			} else {
				JFXButton btnOk = new JFXButton("OK");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnOk), "No such Book Exists in the Issue Database", null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/*String query = "SELECT * FROM ISSUE WHERE ISBN = '" + isbn + "'";
		ResultSet rs = dbHandler.executeQuery(query);
		
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
				ResultSet rs_book = dbHandler.executeQuery(query);
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
				ResultSet rs_member = dbHandler.executeQuery(query);
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
		
		dataList.getItems().setAll(data);*/
		
	}
	
	@FXML
	private void Ren_RenewBookButton(ActionEvent event) {
		if (!isReadytoSubmit) {
			JFXButton btnCheck = new JFXButton("Go Back and Check");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnCheck),
					"Book Not Selected", "Please select a Book to Submit");
			return;
		}
		
		JFXButton btnYes = new JFXButton("YES");
		btnYes.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventYes) -> {
			String update = "UPDATE ISSUE\n"
					+ "SET issue_time = CURRENT_TIMESTAMP\n"
					+ "WHERE isbn = '" + Ren_txfIsbn.getText() + "'";
			if (dbHandler.executeAction(update)) {
				JFXButton btnSuccess = new JFXButton("Done. Go Back");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnSuccess), "Book Renewed Successfully", null);
				EnableDisableControls(false);
				RenSub_DataContainer.setOpacity(0);
			}
			else {
				JFXButton btnFail = new JFXButton("Go Back and Check");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnFail), "Book Renew Opeartion Failed", null);
			}
		});
		
		JFXButton btnNo = new JFXButton("NO");
		btnNo.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventNo) -> {
			JFXButton btnCancelled = new JFXButton("OK. Go Back");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnCancelled), "Renew Operation was Cancelled", null);			
		});
		
		ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnYes, btnNo),
				"Confirm Renew Operation", 
				"Are you sue you want to Renew the book \"" + BoxBook_Name.getText() + "\"?");
	}
	
	@FXML
	private void Ren_SubmitBookButton(ActionEvent event) {
		/*
		 * This section of the code handles the Events
		 * fired when the User clicks on the Submit Book
		 * button in the Renew/Submission tab of the Application.
		 * 
		 * The algorithm first checks if the value of isReadytoSubmit
		 * is true i.e. if the Book is issued and the entry is present
		 * in the Issue. If yes then the book can be Submitted. If this
		 * case is false then the algorithm returns back to start
		 * execution again and for an event to get fired.
		 * 
		 * Further if all goes well then the application asks a
		 * confirmation from the user if s/he confirms to submit
		 * the book back to Library. If the User clicks OK then the 
		 * Book is Submitted, else if the User cancels the
		 * operation then the application returns back to the
		 * previous state.
		 * 
		 * If anywhere any kind of error occurs then the application
		 * notifies the User about this.
		 * 
		 * Continuing with all the operations the algorithm also makes 
		 * changes within the database in order for making the Book 
		 * available again and deleting the Issue record.
		 * 
		 * 
		 * **** ADD A TRIGGER PROCEDURE HERE IF POSSIBLE TO STORE THE DELETED DATA FROM THE ISSUE TABLE ****
		 * 									DO IT IN THE NEW VERSION
		 * 
		 */
		if (!isReadytoSubmit) {
			JFXButton btnCheck = new JFXButton("Go Back and Check");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnCheck),
					"Book Not Selected", "Please select a Book to Submit");
			return;
		}
		
		JFXButton btnYes = new JFXButton("YES");
		btnYes.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventYes) -> {
			String isbn = Ren_txfIsbn.getText();
			String del_query = "DELETE FROM ISSUE WHERE ISBN = '" + isbn + "'";
			String update_query = "UPDATE BOOK SET AVAILABLE = TRUE WHERE ISBN = '" + isbn + "'";
			
			if (dbHandler.executeAction(del_query) && dbHandler.executeAction(update_query)) {
				JFXButton btnSuccess = new JFXButton("Done. Go Back");
				btnSuccess.setOnAction((actionEvent) -> {
					Ren_txfIsbn.requestFocus();
				});
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnSuccess), "Book has been Submitted", null);
				EnableDisableControls(false);
				RenSub_DataContainer.setOpacity(0);
			} else {
				JFXButton btnFail = new JFXButton("Go Back and Check");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnFail), "Book Submission Failed", null);
			}
			clearRenewTabEntries();
		});
		
		JFXButton btnNo = new JFXButton("NO");
		btnNo.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventNo) -> {
			JFXButton btnCancelled = new JFXButton("OK. Go Back");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnCancelled), "Submission Operation was Cancelled", null);			
		});
		
		ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnYes, btnNo),
				"Confirm Submission Operation", 
				"Are you sue you want to Return the book \"" + BoxBook_Name.getText() + "\"?");
	}
}
