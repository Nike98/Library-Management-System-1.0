package library.ui.listBook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.alert.ThrowAlert;
import library.database.handler.DatabaseHandler;

public class ListBookController implements Initializable{
	
	ObservableList<Book> BookList = FXCollections.observableArrayList();

	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private TableView<Book> MainTable;
	
	@FXML
	private TableColumn<Book, String> colIsbn;
	
	@FXML
	private TableColumn<Book, String> colTitle;
	
	@FXML
	private TableColumn<Book, String> colAuthor;
	
	@FXML
	private TableColumn<Book, String> colEdition;
	
	@FXML
	private TableColumn<Book, String> colPublisher;
	
	@FXML
	private TableColumn<Book, Integer> colPrice;
	
	@FXML
	private TableColumn<Book, String> colAvail;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeCol();
		LoadData();
	}
	
	@FXML
	private void DeleteBookOperation(ActionEvent event) {
		Book selectedForDeletion = MainTable.getSelectionModel().getSelectedItem();
		if (selectedForDeletion == null) {
			ThrowAlert.showErrorMessage("Error", "No Book Selected. Please select a proper Book row for Deletion");
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setTitle("Deleting Book");
		alert.setContentText("Are you sure you want to Delete the Book " + selectedForDeletion.getTitle() + " ?");
		Optional<ButtonType> response = alert.showAndWait();
		if (response.get() == ButtonType.OK) {
			boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
			if (result) {
				ThrowAlert.showInformationMessage("Operation Successful", "The Book " + selectedForDeletion.getTitle() + 
						" was Deleted from the Records Successfully");
				BookList.remove(selectedForDeletion);
			} else
				ThrowAlert.showErrorMessage("Error Occured", "The Book " + selectedForDeletion.getTitle() + 
						" could not be Deleted from the Records");
		} else
			ThrowAlert.showErrorMessage("Operation Cancelled", "Deletion Operation Cancelled by the User");
	}

	private void initializeCol() {
		colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
		colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
		colEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
		colPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colAvail.setCellValueFactory(new PropertyValueFactory<>("availibility"));
	}
	
	private void LoadData() {
		DatabaseHandler dataHandler = DatabaseHandler.getInstance();
		String qry = "SELECT * FROM BOOK";
		ResultSet res = dataHandler.exeQuery(qry);
		
		try {
			while (res.next()) {
				String isbn = res.getString("isbn");
				String title = res.getString("title");
				String author = res.getString("author");
				String edition = res.getString("edition_number");
				String publisher = res.getString("publisher");
				Integer price = res.getInt("price");
				Boolean avail = res.getBoolean("available");
				
				BookList.add(new Book(isbn, title, author, edition, publisher, price, avail));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MainTable.setItems(BookList);;
	}
	
	public static class Book{
		private final SimpleStringProperty isbn;
		private final SimpleStringProperty title;
		private final SimpleStringProperty author;
		private final SimpleStringProperty edition;
		private final SimpleStringProperty publisher;
		private final SimpleIntegerProperty price;
		private final SimpleBooleanProperty availibility;
		
		Book ( String isbn, String title, String author, String edition, 
				String publisher, Integer price, Boolean availability ){
			this.isbn = new SimpleStringProperty(isbn);
			this.title = new SimpleStringProperty(title);
			this.author = new SimpleStringProperty(author);
			this.edition = new SimpleStringProperty(edition);
			this.publisher = new SimpleStringProperty(publisher);
			this.price = new SimpleIntegerProperty(price);
			this.availibility = new SimpleBooleanProperty(availability);
		}
		
		public String getIsbn() {
			return isbn.get();
		}
		
		public String getTitle() {
			return title.get();
		}

		public String getAuthor() {
			return author.get();
		}

		public String getEdition() {
			return edition.get();
		}

		public String getPublisher() {
			return publisher.get();
		}

		public Integer getPrice() {
			return price.get();
		}

		public Boolean getAvailibility() {
			return availibility.get();
		}
	}
}
