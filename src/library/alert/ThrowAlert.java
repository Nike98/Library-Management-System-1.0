package library.alert;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.awt.SystemTray;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;


public class ThrowAlert {

	public static void showInformationMessage(String title, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		alert.showAndWait();
	}
	
	public static void showErrorMessage(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static void showErrorMessage(Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Occured");
		alert.setHeaderText("Error Occured");
		alert.setContentText(ex.getLocalizedMessage());
		
		StringWriter strwr = new StringWriter();
		PrintWriter printwr = new PrintWriter(strwr);
		ex.printStackTrace(printwr);
		String exceptionText = strwr.toString();
		
		Label label = new Label("The exception stacktrace was : ");
		
		TextArea txtArea = new TextArea(exceptionText);
		txtArea.setEditable(false);
		txtArea.setWrapText(true);
		txtArea.setMaxWidth(Double.MAX_VALUE);
		txtArea.setMaxHeight(Double.MAX_VALUE);
		
		GridPane.setVgrow(txtArea, Priority.ALWAYS);
		GridPane.setHgrow(txtArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(txtArea, 0, 1);
		
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}

	public static void showErrorMessage(Exception ex, String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Occured");
		alert.setHeaderText(title);
		alert.setContentText(content);
		
		StringWriter strwr = new StringWriter();
		PrintWriter printwr = new PrintWriter(strwr);
		ex.printStackTrace(printwr);
		String exceptionText = strwr.toString();
		
		Label label = new Label("The exception stacktrace was : ");
		
		TextArea txtArea = new TextArea(exceptionText);
		txtArea.setEditable(false);
		txtArea.setWrapText(true);
		txtArea.setMaxWidth(Double.MAX_VALUE);
		txtArea.setMaxHeight(Double.MAX_VALUE);
		
		GridPane.setVgrow(txtArea, Priority.ALWAYS);
		GridPane.setHgrow(txtArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(txtArea, 0, 1);
		
		alert.getDialogPane().setExpandableContent(expContent);
		alert.showAndWait();
	}

	public static void showMaterialDialog(StackPane root, Node nodeToBeBlurred, List<JFXButton> controls, String header, String body) {
		BoxBlur blur = new BoxBlur(3, 3, 3);
		if (controls.isEmpty())
			controls.add(new JFXButton("Okay"));
		
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);
		
		controls.forEach(controlButton ->{
			controlButton.getStyleClass().add("dialog-button");
			controlButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
		});
		
		dialogLayout.setHeading(new Label(header));
		dialogLayout.setBody(new Label(body));
		dialogLayout.setActions(controls);
		dialog.show();
		dialog.setOnDialogClosed((JFXDialogEvent event) -> {
			nodeToBeBlurred.setEffect(null);
		});
		nodeToBeBlurred.setEffect(blur);
	}
	
	public static void showSysTrayMessage(String title, String message) {
		SystemTray sysTray = SystemTray.getSystemTray();
		//BufferedImage image
		// Add all this when custom image of the app is created
	}
}