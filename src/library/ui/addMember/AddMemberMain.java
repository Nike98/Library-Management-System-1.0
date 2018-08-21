package library.ui.addMember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddMemberMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("addMember.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/Resources/Stylesheet/MainStyleSheet.css").toExternalForm());
			primaryStage.setTitle("Add Member");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}


}
