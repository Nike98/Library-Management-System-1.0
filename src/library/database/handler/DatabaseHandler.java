
package library.database.handler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import library.ui.listBook.ListBookController.Book;
import library.ui.listMember.ListMemberController;

public final class DatabaseHandler {
	
	private static DatabaseHandler handler = null;
	
	// Declaring the major Variables to be used throughout the class
	private static final String DB_URL = "jdbc:derby:libraryDB;create=true";
	private static Connection conn = null;
	private static Statement stmt = null;
	
	static {
		CreateConnection();
		inflateDB();
	}
	
	// Constructor
	private DatabaseHandler() {
//		CreateConnection();
//		setupBookTable();
//		setupMemberTable();
//		setupIssueTable();
	}
	
	public static void main(String[] args) {
		DatabaseHandler.getInstance();
	}
	
	public static DatabaseHandler getInstance() {
		if (handler == null)
			handler = new DatabaseHandler();
		
		return handler;
	}
		
	// Method to make the Connection to the Oracle Database
	private static void CreateConnection() {
		
        try {
        	Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
			conn = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Cannot load Derby Database", "Database Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	private static void inflateDB() {
		List<String> tableData = new ArrayList<>();
		try {
			Set<String> loadedTables = getDBTables();
			System.out.println("Already loaded Tables " + loadedTables);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(DatabaseHandler.class.getClass().getResourceAsStream("/Resources/database/tables.xml"));
			NodeList nodeList = doc.getElementsByTagName("table-entry");
			for (int i = 0 ; i < nodeList.getLength() ; i++) {
				Node node = nodeList.item(i);
				Element entry = (Element) node;
				String tableName = entry.getAttribute("name");
				String query = entry.getAttribute("col-data");
				if (!loadedTables.contains(tableName.toLowerCase()))
					tableData.add(String.format("CREATE TABLE %s (%s)", tableName, query));
			}
			if (tableData.isEmpty())
				System.out.println("Tables are already loaded");
			else {
				System.out.println("Inflating new tables");
				createTables(tableData);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static void createTables(List<String> tableData) throws SQLException {
		Statement statement = conn.createStatement();
		statement.closeOnCompletion();
		for (String command: tableData) {
			System.out.println(command);
			statement.addBatch(command);
		}
		statement.executeBatch();
	}
	
	private static Set<String> getDBTables() throws SQLException{
		Set<String> set = new HashSet<>();
		DatabaseMetaData dbMetaData = conn.getMetaData();
		readDBTables(set, dbMetaData, "TABLE", null);
		return set;
	}
	
	private static void readDBTables(Set<String> set, DatabaseMetaData dbMetaData, String searchCriteria, String schema) throws SQLException {
		ResultSet rs = dbMetaData.getTables(null, schema, null, new String[]{searchCriteria});
		while (rs.next())
			set.add(rs.getString("TABLE_NAME").toLowerCase());
	}
	
	public boolean deleteBook(Book book) {
		try {
			String deleteQuery = "DELETE FROM BOOK WHERE ISBN = ?";
			PreparedStatement stmt = conn.prepareStatement(deleteQuery);
			stmt.setString(1, book.getIsbn());
			int res = stmt.executeUpdate();
			if (res == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteMember(ListMemberController.Member member) {
		try {
			String deleteStatement = "DELETE FROM MEMBER WHERE ID= ?";
			PreparedStatement stmt = conn.prepareStatement(deleteStatement);
			stmt.setInt(1, member.getId());
			int result = stmt.executeUpdate();
			if (result == 1)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean isBookIssued(Book book) {
		try {
			String checkIssue = "SELECT COUNT(*) FROM ISSUE WHERE ISBN = ?";
			PreparedStatement stmt = conn.prepareStatement(checkIssue);
			stmt.setString(1, book.getIsbn());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println(count);
				if (count > 0)
					return true;
				else 
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkMemberIssueStatus(ListMemberController.Member member) {
		try {
			String checkStatus = "SELECT COUNT(*) FROM ISSUE WHERE MEMBER_ID = ?";
			PreparedStatement stmt = conn.prepareStatement(checkStatus);
			stmt.setInt(1, member.getId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return (count > 0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updatebook(Book book) {
		try {
			String update = "UPDATE BOOK SET \n"
					+ "TITLE = ?, \n"
					+ "AUTHOR = ?, \n"
					+ "PUBLISHER = ?, \n"
					+ "EDITION_NUMBER = ?, \n"
					+ "PRICE = ? \n"
					+ "WHERE ISBN = ?";
			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getPublisher());
			stmt.setString(4, book.getEdition());
			stmt.setString(5, book.getPrice());
			stmt.setString(6, book.getIsbn());
			int res = stmt.executeUpdate();
			return (res > 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateMember(ListMemberController.Member member) {
		try {
			String update = "UPDATE MEMBER SET \n"
					+ "NAME = ?, \n"
					+ "CITY = ?, \n"
					+ "ADDRESS = ?, \n"
					+ "MOBILE_NO = ?, \n"
					+ "EMAIL_ID = ?, \n"
					+ "WHERE ID = ?";
			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.setString(1, member.getName());
			stmt.setString(2, member.getCity());
			stmt.setString(3, member.getAddress());
			stmt.setString(4, member.getMobile());
			stmt.setString(5, member.getEmail());
			stmt.setInt(6, member.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ObservableList<PieChart.Data> getBookStatistics() {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
		try {
			String bookQuery = "SELECT COUNT(*) FROM BOOK";
			String issueQuery = "SELECT COUNT(*) FROM ISSUE";
			ResultSet rs = executeQuery(bookQuery);
			// For Book
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Total Books (" + count + ")", count));
			}
			
			// For Issue
			rs = executeQuery(issueQuery);
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Issued Books (" + count + ")", count));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
	public ObservableList<PieChart.Data> getMemberStatistics() {
		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
		try {
			String memberQuery = "SELECT COUNT(*) FROM MEMBER";
			String issueQuery = "SELECT COUNT(DISTINCT MEMBER_ID) FROM ISSUE";
			ResultSet rs = executeQuery(memberQuery);
			// For Member
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Total Members (" + count + ")", count));
			}
			
			// For Issue
			rs = executeQuery(issueQuery);
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Members with Books (" + count + ")", count));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return data;
	}
	
	// Method to execute a particular query passed as a String to this method
	public ResultSet executeQuery(String query) {
		ResultSet result;
		
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("Exception at executeQuery:DataHandler" + e.getLocalizedMessage());
			return null;
		}
		
		return result;
	}
	
	public boolean executeAction(String query) {
		/*
		 * This method performs particular action given to it
		 * which include insert, select statements.
		 */
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at executeAction:DataHandler" + e.getLocalizedMessage());
			return false;
		}
	}
}
