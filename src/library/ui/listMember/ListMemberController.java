package library.ui.listMember;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
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

public class ListMemberController implements Initializable {
	
	ObservableList<Member> MemberList = FXCollections.observableArrayList();
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private TableView<Member> MainTable;
	
	@FXML
	private TableColumn<Member, String> colId;
	                    
	@FXML               
	private TableColumn<Member, String> colFname;
	                    
	@FXML               
	private TableColumn<Member, String> colLname;
	
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
		colFname.setCellValueFactory(new PropertyValueFactory<>("fname"));
		colLname.setCellValueFactory(new PropertyValueFactory<>("lname"));
		colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
		colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
	}
	
	private void LoadData() {
		DatabaseHandler dataHandler = new DatabaseHandler();
		String qry = "SELECT * FROM MEMBER";
		ResultSet res = dataHandler.exeQuery(qry);
		
		try {
			while (res.next()) {
				String id = res.getString("id");
				String fname = res.getString("fname");
				String lname = res.getString("lname");
				String city = res.getString("city");
				String address = res.getString("address");
				String email = res.getString("email_id");
				String mobile = res.getString("mobile_no");
				
				MemberList.add(new Member(id, fname, lname, city, address, email, mobile));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MainTable.getItems().setAll(MemberList);
	}

	public static class Member {
		private final SimpleStringProperty id;
		private final SimpleStringProperty fname;
		private final SimpleStringProperty lname;
		private final SimpleStringProperty city;
		private final SimpleStringProperty address;
		private final SimpleStringProperty email;
		private final SimpleStringProperty mobile;
		
		Member ( String id, String fname, String lname, String city, 
				String address, String email, String mobile ){
			this.id = new SimpleStringProperty(id);
			this.fname = new SimpleStringProperty(fname);
			this.lname = new SimpleStringProperty(lname);
			this.city = new SimpleStringProperty(city);
			this.address = new SimpleStringProperty(address);
			this.email = new SimpleStringProperty(email);
			this.mobile = new SimpleStringProperty(mobile);
		}
		
		public String getId() {
			return id.get();
		}
		
		public String getFirstName() {
			return fname.get();
		}

		public String getLastName() {
			return lname.get();
		}

		public String getCity() {
			return city.get();
		}

		public String getAddress() {
			return address.get();
		}

		public String getEmail() {
			return email.get();
		}

		public String getMobile() {
			return mobile.get();
		}
	}
}
