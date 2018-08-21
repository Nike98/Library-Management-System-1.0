package library.ui.listBook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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
		DatabaseHandler dataHandler = new DatabaseHandler();
		String qry = "SELECT * FROM BOOK";
		ResultSet res = dataHandler.exeQuery(qry);
		
		try {
			while (res.next()) {
				String isbn = res.getString("isbn");
				String title = res.getString("title");
				String author = res.getString("author");
				String edition = res.getString("edition_number");
				String publisher = res.getString("publisher");
				int price = res.getInt("price");
				String avail = res.getString("available");
				
				BookList.add(new Book(isbn, title, author, edition, publisher, price, avail));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MainTable.getItems().setAll(BookList);
	}
	
	public static class Book{
		private final SimpleStringProperty isbn;
		private final SimpleStringProperty title;
		private final SimpleStringProperty author;
		private final SimpleStringProperty edition;
		private final SimpleStringProperty publisher;
		private final SimpleIntegerProperty price;
		private final SimpleStringProperty availibility;
		
		Book ( String isbn, String title, String author, String edition, 
				String publisher, int price, String availability ){
			this.isbn = new SimpleStringProperty(isbn);
			this.title = new SimpleStringProperty(title);
			this.author = new SimpleStringProperty(author);
			this.edition = new SimpleStringProperty(edition);
			this.publisher = new SimpleStringProperty(publisher);
			this.price = new SimpleIntegerProperty(price);
			this.availibility = new SimpleStringProperty(availability);
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

		public int getPrice() {
			return price.get();
		}

		public String getAvailibility() {
			return availibility.get();
		}
	}
}
