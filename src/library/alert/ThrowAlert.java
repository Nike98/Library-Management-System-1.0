package library.alert;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
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
		styleAlert(alert);
		alert.showAndWait();
	}
	
	public static void showErrorMessage(String title, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		styleAlert(alert);
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
		
		styleAlert(alert);
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
	
	public static void showDialog(StackPane root, Node blurNode, List<JFXButton> btnControls, String header, String body) {
		JFXDialogLayout dialogLayout = new JFXDialogLayout();
		JFXDialog dialog = new JFXDialog(root, dialogLayout, JFXDialog.DialogTransition.TOP);
		BoxBlur blurEffect = new BoxBlur(4, 4, 4);
		blurNode.setDisable(true);
		
		btnControls.forEach(button -> {
			button.getStyleClass().add("dialog-button");
			button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
				dialog.close();
			});
		});
		
		dialogLayout.setHeading(new Label(header));
		dialogLayout.setBody(new Label(body));
		dialogLayout.setActions(btnControls);
		dialog.show();
		root.requestFocus();
		dialog.setOnDialogClosed((JFXDialogEvent closeEvent) -> {
			blurNode.setEffect(null);
			blurNode.setDisable(false);
		});
		blurNode.setEffect(blurEffect);
	}
	
	private static void styleAlert(Alert alert) {
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(ThrowAlert.class.getResource("/Resources/Stylesheets/LibraryStyleSheet.css").toExternalForm());
		dialogPane.getStyleClass().add("custom-alert");
	}
}
