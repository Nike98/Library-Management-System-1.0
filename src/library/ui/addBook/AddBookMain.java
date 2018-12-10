package library.ui.addbook;
	
import java.sql.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import library.database.handler.*;

public class AddBookMain extends Application {
	
	// Object to Connection class from the java.sql library
	static Connection connection = null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("addBook.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("addBook.css").toExternalForm());
			primaryStage.setTitle("Add Book");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} 
		catch(Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
		// Main connection to the DatabaseHandlerMain Class from the library.database.handler package
		connection = DatabaseHandlerMain.CreateConnection();
	}
}
