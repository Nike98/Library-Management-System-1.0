package library.ui.listMember;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.alert.ThrowAlert;
import library.database.handler.DatabaseHandler;
import library.ui.addMember.AddMemberController;

public class ListMemberController implements Initializable {
	
	private ObservableList<Member> MemberList = FXCollections.observableArrayList();
	private DatabaseHandler dbHandler;
	
	@FXML
	private StackPane rootPane;
	
	@FXML
	private AnchorPane rootAnchorPane;
	
	@FXML
	private TableView<Member> MainTable;
	
	@FXML
	private TableColumn<Member, String> colId;
	                    
	@FXML               
	private TableColumn<Member, String> colName;
	
	@FXML               
	private TableColumn<Member, String> colCity;
	                    
	@FXML               
	private TableColumn<Member, String> colAddress;
	                    
	@FXML               
	private TableColumn<Member, String> colEmail;
	                    
	@FXML               
	private TableColumn<Member, String> colMobile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeCol();
		LoadData();
	}
	
	private void initializeCol() {
		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
	}
	
	private Stage getStage() {
		return (Stage) MainTable.getScene().getWindow();
	}
	
	private void LoadData() {
		MemberList.clear();
		DatabaseHandler dataHandler = DatabaseHandler.getInstance();
		String qry = "SELECT * FROM MEMBER";
		ResultSet res = dataHandler.exeQuery(qry);
		try {
			while (res.next()) {
				Integer id = res.getInt("id");
				String name = res.getString("name");
				String city = res.getString("city");
				String address = res.getString("address");
				String mobile = res.getString("mobile_no");
				String email = res.getString("email_id");
				
				MemberList.add(new Member(id, name, city, address, mobile, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MainTable.getItems().setAll(MemberList);
	}
	
	@FXML
	private void refreshOperation(ActionEvent event) {
		LoadData();
	}
	
	@FXML
	private void deleteMemberOperation(ActionEvent event) {
		ListMemberController.Member selectedForDeletion = MainTable.getSelectionModel().getSelectedItem();
		if (selectedForDeletion == null) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"No Member Selected", "Please select a proper member row to be deleted");
			return;
		}
		if (dbHandler.checkMemberIssueStatus(selectedForDeletion)) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"Deletion Error", "This member cannot be deleted as the member has book(s) issued to himself");
			return;
		}
		
		JFXButton btnYes = new JFXButton("YES");
		JFXButton btnCancel = new JFXButton("CANCEL");
		btnYes.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventYes) -> {
			boolean result = dbHandler.deleteMember(selectedForDeletion);
			if (result) {
				JFXButton btnDone = new JFXButton("Done. Go Back");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnDone),
						"Deletion Successful",
						"Member " + selectedForDeletion.getName() + " has been deleted successfully");
				MemberList.remove(selectedForDeletion);
			} else {
				JFXButton btnBack = new JFXButton("Go Back and Check");
				ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnBack),
						"Deletion Failed",
						"Member " + selectedForDeletion.getName() + " could not be deleted");
			}
		});
		btnCancel.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent eventCancel) -> {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"Operaton Cancelled",
					"Member deletion was cancelled by the user");
		});
		ThrowAlert.showDialog(rootPane, rootAnchorPane, Arrays.asList(btnYes, btnCancel),
				"Confirm Delete Operation",
				"Are you sure you want to delete the member " + selectedForDeletion.getName());
	}
	
	@FXML
	private void editMemberOperation(ActionEvent event) {
		Member selectedForEdit = MainTable.getSelectionModel().getSelectedItem();
		if (selectedForEdit == null) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"No Member Selected", "Please select a proper member row to edit");
			return;
		}
		if (dbHandler.checkMemberIssueStatus(selectedForEdit)) {
			ThrowAlert.showDialog(rootPane, rootAnchorPane, new ArrayList<>(),
					"Member Edit Error", "This member data cannot be edited as the member has book(s) issued to himself");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/ui/addMember/addMember.fxml"));
			Parent parent = loader.load();
			
			AddMemberController addMemberController = (AddMemberController) loader.getController();
			addMemberController.inflateUI(selectedForEdit);
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@FXML
	private void exportAsPDF(ActionEvent event) {
		
	}
	
	@FXML
	private void closeStage(ActionEvent event) {
		getStage().close();
	}

	public static class Member {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty name;
		private final SimpleStringProperty city;
		private final SimpleStringProperty address;
		private final SimpleStringProperty mobile;
		private final SimpleStringProperty email;
		
		public Member ( Integer id, String name, String city, 
				String address, String mobile, String email){
			this.id = new SimpleIntegerProperty(id);
			this.name = new SimpleStringProperty(name);
			this.city = new SimpleStringProperty(city);
			this.address = new SimpleStringProperty(address);
			this.mobile = new SimpleStringProperty(mobile);
			this.email = new SimpleStringProperty(email);
		}
		
		public Integer getId() {
			return id.get();
		}
		
		public String getName() {
			return name.get();
		}

		public String getCity() {
			return city.get();
		}

		public String getAddress() {
			return address.get();
		}
		
		public String getMobile() {
			return mobile.get();
		}

		public String getEmail() {
			return email.get();
		}
	}
}
