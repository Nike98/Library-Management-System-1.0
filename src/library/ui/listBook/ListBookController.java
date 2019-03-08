package library.ui.listBook;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.alert.ThrowAlert;
import library.database.handler.DatabaseHandler;
import library.ui.addBook.AddBookController;
import library.util.LibraryUtil;

public class ListBookController implements Initializable{
	
	private ObservableList<Book> BookList = FXCollections.observableArrayList();
	private DatabaseHandler dbHandler = DatabaseHandler.getInstance();

	@FXML
	private StackPane rootPane;
	
	@FXML
	private AnchorPane rootAnchorPane;
	
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
	
	private Stage getStage() {
		return (Stage) MainTable.getScene().getWindow();
	}
	
	private void LoadData() {
		BookList.clear();
		String query = "SELECT * FROM BOOK";
		ResultSet res = dbHandler.executeQuery(query);
		
		try {
			while (res.next()) {
				String isbn = res.getString("isbn");
				String title = res.getString("title");
				String author = res.getString("author");
				String edition = res.getString("edition_number");
				String publisher = res.getString("publisher");
				String price = res.getString("price");
				Boolean avail = res.getBoolean("available");
				
				BookList.add(new Book(isbn, title, author, edition, publisher, price, avail));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MainTable.setItems(BookList);;
	}
	
	@FXML
	private void refreshTableOperation(ActionEvent event) {
		LoadData();
	}
	
	@FXML
	private void editBookOperation(ActionEvent event) {
		Book selectedForEdit = MainTable.getSelectionModel().getSelectedItem();
		if (selectedForEdit == null) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"No Book Selected", "Please select a proper Book row to edit");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/ui/addbook/addBook.fxml"));
			Parent parent = loader.load();
			
			AddBookController addBookController = (AddBookController) loader.getController();
			addBookController.inflateUI(selectedForEdit);
			
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit Book");
			stage.setScene(new Scene(parent));
			stage.show();
			// Show the Stage icon here
			
			stage.setOnHiding((e) -> {
				refreshTableOperation(new ActionEvent());
			});
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@FXML
	private void deleteBookOperation(ActionEvent event) {
		Book selectedForDeletion = MainTable.getSelectionModel().getSelectedItem();
		if (selectedForDeletion == null) {
			//ThrowAlert.showErrorMessage("Error", "No Book Selected. Please select a proper Book row for Deletion");
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"No Book Selected", "Please select a proper Book row for deletion");
			return;
		}
		/*Alert alert = new Alert(AlertType.CONFIRMATION);
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
			ThrowAlert.showErrorMessage("Operation Cancelled", "Deletion Operation Cancelled by the User");*/
		
		if (DatabaseHandler.getInstance().isBookIssued(selectedForDeletion)) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"Deletion Error", "This book can't be deleted as it is already issued");
			return;
		}
		
		JFXButton btnYes = new JFXButton("YES");
		JFXButton btnCancel = new JFXButton("CANCEL");
		btnYes.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventYes) -> {
			boolean result = dbHandler.deleteBook(selectedForDeletion);
			if (result) {
				JFXButton btnDone = new JFXButton("Done. Go Back");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnDone),
						"Deletioln Successful",
						"Book \"" + selectedForDeletion.getTitle() + "\" has been deleted successfully");
				BookList.remove(selectedForDeletion);
			} else {
				JFXButton btnBack = new JFXButton("Go Back and Check");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnBack),
						"Deletion Failed",
						selectedForDeletion.getTitle() + "could not be deleted");
			}
		});
		btnCancel.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventCancel) -> {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(), 
					"Operation Cancelled", 
					"Book deletion operation was cancelled by the user");
		});
		ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnYes, btnCancel),
				"Confirm Delete Operation",
				"Are you sure you want to delete the book \"" + selectedForDeletion.getTitle() + "\"");
	}
	
	@FXML
	private void exportAsPDF(ActionEvent event) {
		List<List> printData = new ArrayList<>();
		String[] headers = {
				"  ISBN     ",
				"       Title        ",
				"   Author     ",
				"   Edition No.     ",
				"   Publisher     ",
				"Price   ",
				"Availability"
		};
		printData.add(Arrays.asList(headers));
		for (Book book : BookList) {
			List<String> row = new ArrayList<>();
			row.add(book.getIsbn());
			row.add(book.getTitle());
			row.add(book.getAuthor());
			row.add(book.getEdition());
			row.add(book.getPublisher());
			row.add(book.getPrice());
			row.add(book.getAvailibility());
			printData.add(row);
		}
		LibraryUtil.initPDFExport(rootPane, rootAnchorPane, getStage(), printData, "Book");
	}
	
	@FXML
	private void closeStage() {
		getStage().close();
	}
	
	public static class Book{
		private final SimpleStringProperty isbn;
		private final SimpleStringProperty title;
		private final SimpleStringProperty author;
		private final SimpleStringProperty edition;
		private final SimpleStringProperty publisher;
		private final SimpleStringProperty price;
		private final SimpleStringProperty availibility;
		
		public Book ( String isbn, String title, String author, String edition, 
				String publisher, String price, Boolean availability ) {
			this.isbn = new SimpleStringProperty(isbn);
			this.title = new SimpleStringProperty(title);
			this.author = new SimpleStringProperty(author);
			this.edition = new SimpleStringProperty(edition);
			this.publisher = new SimpleStringProperty(publisher);
			this.price = new SimpleStringProperty(price);
			//this.availibility = new SimpleBooleanProperty(availability);
			if (availability)
				this.availibility = new SimpleStringProperty("Available");
			else
				this.availibility = new SimpleStringProperty("Issued");
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

		public String getPrice() {
			return price.get();
		}

		public String getAvailibility() {
			return availibility.get();
		}
	}
}
