package library.ui.issueList;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.database.handler.DatabaseHandler;
import library.util.LibraryUtil;

public class IssueListController implements Initializable {
	
	ObservableList<Issue> IssueList = FXCollections.observableArrayList();
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private TableView<Issue> tableView;
	
	@FXML
	private TableColumn<Issue, Integer> colSrNo;
	
	@FXML
	private TableColumn<Issue, String> colIsbn;
	
	@FXML
	private TableColumn<Issue, String> colBookName;
	
	@FXML
	private TableColumn<Issue, String> colMemberName;
	
	@FXML
	private TableColumn<Issue, String> colIssueTime;
	
	@FXML
	private TableColumn<Issue, Integer> colDays;
	
	@FXML
	private TableColumn<Issue, Integer> colFine;
 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeCol();
		LoadData();
	}
	
	private void initializeCol() {
		colSrNo.setCellValueFactory(new PropertyValueFactory<>("id"));
		colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		colBookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
		colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
		colIssueTime.setCellValueFactory(new PropertyValueFactory<>("issue_time"));
		colDays.setCellValueFactory(new PropertyValueFactory<>("days"));
		colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
		tableView.setItems(IssueList);
	}
	
	private void LoadData() {
		IssueList.clear();
		DatabaseHandler dbHandler = DatabaseHandler.getInstance();
		String query = "SELECT ISSUE.ISBN, ISSUE.MEMBER_ID, ISSUE.ISSUE_TIME, \n"
				+ "MEMBER.NAME, BOOK.TITLE FROM ISSUE \n"
				+ "LEFT OUTER JOIN MEMBER \n"
				+ "ON MEMBER.ID = ISSUE.MEMBER_ID \n"
				+ "LEFT OUTER JOIN BOOK \n"
				+ "ON BOOK.ISBN = ISSUE.ISBN";
		ResultSet rs = dbHandler.exeQuery(query);
		
		try {
			int counter = 0;
			while (rs.next()) {
				counter += 1;
				String isbn = rs.getString("isbn");
				String bookTitle = rs.getString("title");
				String memName = rs.getString("name");
				Timestamp issueTime = rs.getTimestamp("issue_time");
				Integer days = Math.toIntExact(TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - issueTime.getTime()));
				Double fine = LibraryUtil.getFineAmount(days);
				Issue issueInfo = new Issue(counter, isbn, bookTitle, memName, LibraryUtil.formatDateTimeString(new Date(issueTime.getTime())), days, fine);
				IssueList.add(issueInfo);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static class Issue {		
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty isbn;
		private final SimpleStringProperty bookName;
		private final SimpleStringProperty memName;
		private final SimpleStringProperty issueTime;
		private final SimpleIntegerProperty days;
		private final SimpleDoubleProperty fine;
		
		Issue (Integer id, String isbn, String bookName, String memName, 
				String issueTime, Integer days, Double fine){
			this.id = new SimpleIntegerProperty(id);
			this.isbn = new SimpleStringProperty(isbn);
			this.bookName = new SimpleStringProperty(bookName);
			this.memName = new SimpleStringProperty(memName);
			this.issueTime = new SimpleStringProperty(issueTime);
			this.days = new SimpleIntegerProperty(days);
			this.fine = new SimpleDoubleProperty(fine);
		}

		public Integer getId() {
			return id.get();
		}

		public String getIsbn() {
			return isbn.get();
		}
		
		public String getBookName() {
			return bookName.get();
		}
		
		public String getMemName() {
			return memName.get();
		}

		public String getIssueTime() {
			return issueTime.get();
		}

		public Integer getDays() {
			return days.get();
		}
		
		public Double getFine() {
			return fine.get();
		}
	}
}
