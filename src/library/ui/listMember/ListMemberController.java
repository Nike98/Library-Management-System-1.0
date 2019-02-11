package library.ui.listMember;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
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
	
	private void LoadData() {
		DatabaseHandler dataHandler = DatabaseHandler.getInstance();
		String qry = "SELECT * FROM MEMBER";
		ResultSet res = dataHandler.exeQuery(qry);
		
		try {
			while (res.next()) {
				Integer id = res.getInt("id");
				String name = res.getString("name");
				String city = res.getString("city");
				String address = res.getString("address");
				Long mobile = res.getLong("mobile_no");
				String email = res.getString("email_id");
				
				MemberList.add(new Member(id, name, city, address, mobile, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MainTable.getItems().setAll(MemberList);
	}

	public static class Member {
		private final SimpleIntegerProperty id;
		private final SimpleStringProperty name;
		private final SimpleStringProperty city;
		private final SimpleStringProperty address;
		private final SimpleLongProperty mobile;
		private final SimpleStringProperty email;
		
		public Member ( Integer id, String name, String city, 
				String address, Long mobile, String email){
			this.id = new SimpleIntegerProperty(id);
			this.name = new SimpleStringProperty(name);
			this.city = new SimpleStringProperty(city);
			this.address = new SimpleStringProperty(address);
			this.mobile = new SimpleLongProperty(mobile);
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
		
		public Long getMobile() {
			return mobile.get();
		}

		public String getEmail() {
			return email.get();
		}
	}
}
