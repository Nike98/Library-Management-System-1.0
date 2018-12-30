
package library.database.handler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import library.ui.listBook.ListBookController.Book;

public final class DatabaseHandler {
	
	private static DatabaseHandler handler = null;
	
	// Declaring the major Variables to be used throughout the class
	private static final String DB_URL = "jdbc:derby:libraryDB;create=true";
	private static Connection conn = null;
	private static Statement stmt = null;
	
	// Constructor
	private DatabaseHandler() {
		CreateConnection();
		setupBookTable();
		setupMemberTable();
		setupIssueTable();
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
		
		/*try {

			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","lib_demo","lib");
			
			return conn;
			
		} catch ( Exception e ) {
			
			JOptionPane.showMessageDialog(null, "Cannot load Oracle Database", "Database Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		return null;*/
	}
	
	public static DatabaseHandler getInstance() {
		if (handler == null)
			handler = new DatabaseHandler();
		
		return handler;
	}
	
	private void setupBookTable() {
		/*
		 * These methods are written to check whether the Specified table 
		 * exists of or not.
		 * 
		 * If it exists then the further code execution continues.
		 * 
		 * If it doesn't exist then it creates the table in the below 
		 * specified manner.
		 */
		String Table_Name = "Book";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbmetadata = conn.getMetaData();
			ResultSet tables = dbmetadata.getTables(null, null, Table_Name.toUpperCase(), null);
			
			if (tables.next())
				System.out.println("Table " + Table_Name + " Exists.");
			else {
				stmt.execute("CREATE TABLE " + Table_Name + " ("
						+ "isbn varchar(17),\n"
						+ "title varchar(200) not null,\n"
						+ "author varchar(60) not null,\n"
						+ "edition_number varchar(60) not null,\n"
						+ "publisher varchar(200) not null,\n"
						+ "price integer not null,\n"
						+ "available boolean default true,\n"
						+ "constraint book_isbn_pk primary key (isbn)"
						+ ")");
				}
		} catch (SQLException e) {
			e.printStackTrace();
			// REMOVE THIS BEFORE DEPLOYMENT
			System.err.println(e.getMessage() + " -- Database setup problem");
		}
	}
	
	private void setupMemberTable() {
		String Table_Name = "Member";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbmetadata = conn.getMetaData();
			ResultSet tables = dbmetadata.getTables(null, null, Table_Name.toUpperCase(), null);
			
			if (tables.next())
				System.out.println("Table " + Table_Name + " Exists.");
			else {
				stmt.execute("CREATE TABLE " + Table_Name + "("
						+ "id integer generated always as identity (start with 100, increment by 1),\n"
						+ "name varchar(200) not null,\n"
						+ "city varchar(100) not null,\n"
						+ "address varchar(200) not null,\n"
						+ "mobile_no integer not null unique,\n"
						+ "email_id varchar(100) not null unique,\n"
						+ "constraint member_id_pk primary key (id)"
						+ ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println(e.getMessage() + " -- Database setup problem");
		}
	}
	
	private void setupIssueTable() {
		
	  /*
	   * These methods are written to check whether the Specified table 
	   * exists of or not. 
	   * 
	   * If it exists then the further code execution continues. 
	   * If it doesn't exist then it creates the table in the below 
	   * specified manner.
	   */
		 
		String Table_Name = "Issue";
		
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbmetadata = conn.getMetaData();
			ResultSet tables = dbmetadata.getTables(null, null, Table_Name.toUpperCase(), null);
			
			if (tables.next())
				System.out.println("Table " + Table_Name + " Exists.");
			else {
				stmt.execute("CREATE TABLE " + Table_Name + "("
						+ "isbn varchar(17),\n"
						+ "member_id integer,\n"
						+ "issue_time timestamp default current_timestamp,\n"
						+ "day_count integer default 0,\n"
						+ "constraint issue_isbn_fk foreign key (isbn) references book(isbn),\n"
						+ "constraint issue_member_id foreign key (member_id) references member(id)"
						+ ")");
				}
		} catch (SQLException e) {
			e.printStackTrace();
			// REMOVE THIS BEFORE DEPLOYMENT
			System.err.println(e.getMessage() + " -- Database setup problem");
		}
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
	
	

	
	// Method to execute a particular query passed as a String to this method
	public ResultSet exeQuery(String query) {
		ResultSet result;
		
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("Exception at exeQuery:DataHandler" + e.getLocalizedMessage());
			return null;
		}
		
		return result;
	}
	
	public boolean exeAction(String qry) {
		/*
		 * This method performs particular action given to it
		 * which include insert, select statements.
		 */
		try {
			stmt = conn.createStatement();
			stmt.execute(qry);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at exeAction:DataHandler" + e.getLocalizedMessage());
			return false;
		}
	}
}
