package library.ui.listMember;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import library.database.handler.DatabaseHandler;

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
<<<<<<< HEAD
		String query = "SELECT * FROM MEMBER";
		ResultSet res = dataHandler.executeQuery(query);
		
=======
		String qry = "SELECT * FROM MEMBER";
		ResultSet res = dataHandler.exeQuery(qry);
>>>>>>> bda6d451e73cc343d1c5700835c32b6feccf01fc
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
		
	}
	
	@FXML
	private void editMemberOperation(ActionEvent event) {
		
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
