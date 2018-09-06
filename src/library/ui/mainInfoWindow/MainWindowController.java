package library.ui.mainInfoWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindowController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
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

}
