package library.ui.mainInfoWindow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public class MainWindowController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
	private void AddBookButton (ActionEvent event) {
		
	}
	
	@FXML
	private void ListBookButton (ActionEvent event) {
		
	}
	
	@FXML
	private void AddMemberButton (ActionEvent event) {
		
	}
	
	@FXML
	private void ListMemberButton (ActionEvent event) {
		
	}
	
	private void LoadWindow (String location, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(location));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
